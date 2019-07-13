package kt.school.starlord.model.repository.mock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kt.school.starlord.domain.repository.CategoriesRepository
import kt.school.starlord.domain.repository.CategoriesWithSubcategoriesRepository
import kt.school.starlord.domain.repository.SubcategoriesRepository
import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory

/**
 * Repository that contains fake (mocked) data from all application data sources.
 */
@Suppress("all")
class MockRepository : CategoriesRepository,
    SubcategoriesRepository,
    CategoriesWithSubcategoriesRepository {
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
            Subcategory("Шкафы. Комоды. Горки. Секции. Полки", categoryName, 3388, ""),
            Subcategory("Диваны. Кресла. Мягкая мебель", categoryName, 8962, ""),
            Subcategory("Столы. Стулья. Тумбы", categoryName, 2575, ""),
            Subcategory("Кровати. Матрасы. Мебель для спальни", categoryName, 2740, ""),
            Subcategory("Кухни и кухонная мебель", categoryName, 2976, ""),
            Subcategory("Мебель для детской комнаты", categoryName, 4059, ""),
            Subcategory("Мебель для ванной", categoryName, 1904, ""),
            Subcategory("Офисная мебель", categoryName, 4704, ""),
            Subcategory("Элементы интерьера. Дизайн.", categoryName, 17044, ""),
            Subcategory("Постельное белье и принадлежности", categoryName, 11112, ""),
            Subcategory("Посуда и кухонные принадлежности", categoryName, 638, ""),
            Subcategory("Бытовая техника.", categoryName, 2842, ""),
            Subcategory("Бытовая техника: ремонт, подключение и другие услуги", categoryName, 423, "")
        )
    )

    override suspend fun updateSubcategories(subcategories: List<Subcategory>) {}

    override suspend fun getCategoriesWithSubcategories(): Map<Category, List<Subcategory>> = mapOf()
}
