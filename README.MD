## **Архитектура**

Для архитектуры данного приложения я выбрал MVVM
![](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png?hl=ru)

*Структура проекта:*
**data** — пакет содержит классы для отвечающие за хранение данных

**di** — пакет модулей для внедрения зависимойстей

**domain** — внутренняя логика приложения

**other** — функции расширения и утилитарные классы

**ui** — классы пользовательского интерфейса

На слое presentation я реализовал упрощенную архитектуру Flux. Состояние экранов хранится в LiveData и изменяется только через функцию-редьюсер (invoke во ViewModel). Потомки класса [State](https://github.com/Kondenko/FunBoxTestTask/blob/master/app/src/main/java/com/kondenko/funshop/presentation/flux/State.kt) — это все возможные состояния приложения. Потомки [классов](https://github.com/Kondenko/FunBoxTestTask/blob/master/app/src/main/java/com/kondenko/funshop/presentation/flux/Action.kt) Action.Buyer и Action.Admin позволяют перейти от одного состояния к другому.

Благодаря ViewModel можно не переживать о сохранении состояния любой сложности при перевороте экрана.

## Описание экранов
В приложении три экрана:
-детальный просмотр товара
-просмотр списка товаров
-редактирование товара


## Сохранение и загрузка данных

Для хранения я использую Room. При первом создании БД она [заполняется](https://github.com/srgpanov/FunBoxTT/blob/aef6ded3e8c59cabe5523d4721b29d56933bc880/app/src/main/java/com/srgpanov/funboxtt/di/RoomModule.kt#L40) данными из файла data.csv.

Класс [Repository](https://github.com/srgpanov/FunBoxTT/blob/aef6ded3e8c59cabe5523d4721b29d56933bc880/app/src/main/java/com/srgpanov/funboxtt/data/Repository.kt#L11) служит адаптером для источника данных. Дынные поставляются через интерфейс [GoodsStorage](https://github.com/srgpanov/FunBoxTT/blob/master/app/src/main/java/com/srgpanov/funboxtt/data/storage/GoodsStorage.kt). В приложении два источника дынных БД и CSV файл, для каждого написан своя реализация GoodsStorage (для источника данных из CSV файла не имплементирована запись данных).

На эранах [store_front](https://github.com/srgpanov/FunBoxTT/blob/master/app/src/main/java/com/srgpanov/funboxtt/ui/screens/store_front/StoreAdapter.kt) и [back_end](https://github.com/srgpanov/FunBoxTT/blob/master/app/src/main/java/com/srgpanov/funboxtt/ui/screens/back_end/BackendFragment.kt) данные всегда достаются из хранилища через Repository

На эране редактирования товара для сохранения ввёдёных ползователем данных используется  [viewModel](https://github.com/srgpanov/FunBoxTT/blob/master/app/src/main/java/com/srgpanov/funboxtt/ui/screens/goods_redactor/GoodsRedactorViewModel.kt) вместе с SavedStateHandle, чтобы в случае когда приложение будет "убито" системой введённые данные сохранились

## Покупка и сохранение товаров

Если товар был куплен, пока его [редактировали](https://github.com/srgpanov/FunBoxTT/blob/aef6ded3e8c59cabe5523d4721b29d56933bc880/app/src/main/java/com/srgpanov/funboxtt/ui/shared_components/goods_components/GoodsSharedViewModel.kt#L84) появится сообщение, что сохранение не удалось и произошла ошибка.

Если товар был [отредактирован](https://github.com/srgpanov/FunBoxTT/blob/aef6ded3e8c59cabe5523d4721b29d56933bc880/app/src/main/java/com/srgpanov/funboxtt/ui/shared_components/goods_components/GoodsSharedViewModel.kt#L68) и его количество стало <1, в процессе его покупки появится сообщение, что покупка не удалась и произошла ошибка.
