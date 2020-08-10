package com.srgpanov.funboxtt.data.storage

import android.content.Context
import android.util.Log
import com.srgpanov.funboxtt.data.entity.Goods
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssetsStorage @Inject constructor(
    @ApplicationContext private val context: Context
) : GoodsStorage {

    private val assetName = "data.csv"

    private val csvLineRegex = "\"(.*?)\""

    @Suppress("BlockingMethodInNonBlockingContext")
    override fun getAllGoods(): Flow<List<Goods>> = flow{
        val parsedLines = context.assets.open(assetName).parseLines()
        val listGoods = mutableListOf<Goods>()
        for (line in parsedLines) {
            listGoods += line.parseGood() ?: continue
        }
        emit( listGoods)
    }

    //медот будет реализован если в качестве хранилища будет использоваться сохранение в CSV файл
    override suspend fun insertGoods(goods: Goods) {
        TODO("Not yet implemented")
    }

    private suspend fun String.parseGood(): Goods? = withContext(Dispatchers.IO) {
        try {
            val matches = Pattern
                .compile(csvLineRegex)
                .matcher(this@parseGood)
                .allSubgroups()
                .values
                .toList()
            val name = matches[0]
            val price = matches[1]?.toFloatOrNull()
            val quantity = matches[2]?.toIntOrNull()
            if (matches.size != 3) throw IllegalArgumentException("Could't parse string as a good")
            if (name == null) throw IllegalArgumentException("Name not parsed")
            if (price == null) throw IllegalArgumentException("Price not parsed")
            if (quantity == null) throw IllegalArgumentException("Quantity not parsed")
            Goods(null, name, price, quantity)
        } catch (e: Exception) {
            null
        }

    }

    private suspend fun InputStream.parseLines(): List<String> = withContext(Dispatchers.IO) {
        var reader: BufferedReader? = null
        val stringsList = mutableListOf<String>()
        try {
            reader = BufferedReader(InputStreamReader(this@parseLines))
            do {
                val line = reader.readLine()
                if (line != null) {
                    stringsList += line
                }
            } while (line != null)
        } catch (e: IOException) {
            Log.e("AssetsStorage", "parseLines exception: $e")
        } finally {
            try {
                reader?.close()
            } catch (e: IOException) {
                Log.e("AssetsStorage", "parseLines exception: $e")
            }
        }
        return@withContext stringsList
    }

    private fun Matcher.allSubgroups(): Map<String?, String?> {
        val groupsMap = mutableMapOf<String?, String?>()
        while (find()) {
            for (i in 1..groupCount()) {
                group(i)?.let { str ->
                    groupsMap += group() to str
                }
            }
        }
        return groupsMap
    }
}
