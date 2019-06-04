package kt.school.starlord.model

import kt.school.starlord.domain.CategoriesRepository
import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory

class MockRepository : CategoriesRepository {

    override suspend fun getCategories(): List<Category> {
        val subcategories = listOf(Subcategory("name", 15, "link"))
        return listOf(
            Category("Телефоны. Смартфоны", subcategories),
            Category("Ноутбуки. Компьютеры. Apple. Оргтехника", subcategories),
            Category("Ноутбуки", subcategories),
            Category("Планшеты и электронные книги", subcategories),
            Category("Ноутбуки, планшеты, электронные книги: запчасти и аксессуары", subcategories),
            Category("Ноутбуки, планшеты, электронные книги: ремонт и другие услуги", subcategories),
            Category("Apple. Mac. iPod. iPhone. iPad", subcategories),
            Category("Apple: запчасти и аксессуары", subcategories),
            Category("Apple: ремонт и другие услуги", subcategories),
            Category("Материнские платы. Процессоры. Оперативная память", subcategories),
            Category("Видеокарты", subcategories),
            Category("USB-флешки. HDD. SSD. Чистые CD/DVD/Blu-ray диски и приводы", subcategories),
            Category("Другие компьютерные комплектующие", subcategories),
            Category("Корпуса. Блоки питания. ИБП(UPS). Системы охлаждения. Моддинг", subcategories),
            Category("Мониторы. Проекторы", subcategories),
            Category("Акустика. Клавиатуры. Мыши. Аксессуары", subcategories),
            Category("Сетевое оборудование. Модемы. Wi-Fi", subcategories),
            Category("Принтеры. МФУ. Офисная техника", subcategories),
            Category("Заправка картриджей. Ремонт принтеров и другие услуги", subcategories),
            Category("Сборка. Услуги. Сервис. Готовые компьютеры и серверы", subcategories),
            Category("ПО и Игры для PС и Mac", subcategories),
            Category("Радиотехника и электроника", subcategories)
        )
    }
}