package kt.school.starlord.model

import kt.school.starlord.domain.CategoriesRepository
import kt.school.starlord.entity.Category

class MockRepository : CategoriesRepository {

    override fun getCategories(): List<Category> {
        return listOf(
            Category("Телефоны. Смартфоны", 249),
            Category("Ноутбуки. Компьютеры. Apple. Оргтехника", 249),
            Category("Ноутбуки", 64),
            Category("Планшеты и электронные книги", 41),
            Category("Ноутбуки, планшеты, электронные книги: запчасти и аксессуары", 174),
            Category("Ноутбуки, планшеты, электронные книги: ремонт и другие услуги", 3),
            Category("Apple. Mac. iPod. iPhone. iPad", 16),
            Category("Apple: запчасти и аксессуары", 10),
            Category("Apple: ремонт и другие услуги", 1),
            Category("Материнские платы. Процессоры. Оперативная память", 119),
            Category("Видеокарты", 51),
            Category("USB-флешки. HDD. SSD. Чистые CD/DVD/Blu-ray диски и приводы", 47),
            Category("Другие компьютерные комплектующие", 57),
            Category("Корпуса. Блоки питания. ИБП(UPS). Системы охлаждения. Моддинг", 56),
            Category("Мониторы. Проекторы", 24),
            Category("Акустика. Клавиатуры. Мыши. Аксессуары", 48),
            Category("Сетевое оборудование. Модемы. Wi-Fi", 35),
            Category("Принтеры. МФУ. Офисная техника", 51),
            Category("Заправка картриджей. Ремонт принтеров и другие услуги", 9),
            Category("Сборка. Услуги. Сервис. Готовые компьютеры и серверы", 41),
            Category("ПО и Игры для PС и Mac", 7),
            Category("Радиотехника и электроника", 208)
        )
    }
}