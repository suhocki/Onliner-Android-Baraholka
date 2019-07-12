package kt.school.starlord.model.mock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kt.school.starlord.domain.CategoriesRepository
import kt.school.starlord.domain.CategoriesWithSubcategoriesRepository
import kt.school.starlord.domain.SubcategoriesRepository
import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory

/**
 * Repository that contains fake (mocked) data from all application data sources.
 */
class MockRepository : CategoriesRepository, SubcategoriesRepository, CategoriesWithSubcategoriesRepository {
    override fun getCategories(): LiveData<List<Category>> = MutableLiveData<List<Category>>(
        listOf(
            Category("Мобильные телефоны"),
            Category("Компьютеры"),
            Category("Фотоаппараты"),
            Category("Авто"),
            Category("Запчасти"),
            Category("Дом"),
            Category("Строительство"),
            Category("Семья"),
            Category("Работа"),
            Category("Животные"),
            Category("Праздники"),
            Category("Недвижимость"),
            Category("Женская одежда"),
            Category("Мужская одежда")
        )
    )

    override suspend fun updateCategories(categories: List<Category>) {}

    override fun getSubcategories(categoryName: String) = MutableLiveData<List<Subcategory>>(
        listOf(
            Subcategory("Шкафы. Комоды. Горки. Секции. Полки", "", 3388, ""),
            Subcategory("Диваны. Кресла. Мягкая мебель", "", 8962, ""),
            Subcategory("Столы. Стулья. Тумбы", "", 2575, ""),
            Subcategory("Кровати. Матрасы. Мебель для спальни", "", 2740, ""),
            Subcategory("Кухни и кухонная мебель", "", 2976, ""),
            Subcategory("Мебель для детской комнаты", "", 4059, ""),
            Subcategory("Мебель для ванной", "", 1904, ""),
            Subcategory("Офисная мебель", "", 4704, ""),
            Subcategory("Элементы интерьера. Дизайн.", "", 17044, ""),
            Subcategory("Постельное белье и принадлежности", "", 11112, ""),
            Subcategory("Посуда и кухонные принадлежности", "", 638, ""),
            Subcategory("Бытовая техника.", "", 2842, ""),
            Subcategory("Бытовая техника: ремонт, подключение и другие услуги", "", 423, "")
        )
    )

    override suspend fun updateSubcategories(subcategories: List<Subcategory>) {}

    override suspend fun getCategoriesWithSubcategories(): Map<Category, List<Subcategory>> = mapOf()
}
