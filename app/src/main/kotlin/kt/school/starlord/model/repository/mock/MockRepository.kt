@file:Suppress("all")

package kt.school.starlord.model.repository.mock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import kt.school.starlord.domain.entity.category.CategoriesWithSubcategories
import kt.school.starlord.domain.entity.category.Category
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.domain.entity.product.ProductOwner
import kt.school.starlord.domain.entity.product.ProductPrice
import kt.school.starlord.domain.entity.product.ProductType
import kt.school.starlord.domain.entity.subcategory.Subcategory
import kt.school.starlord.domain.repository.CategoriesCacheRepository
import kt.school.starlord.domain.repository.CategoriesWithSubcategoriesRepository
import kt.school.starlord.domain.repository.SubcategoriesRepository
import kt.school.starlord.domain.repository.product.ProductsCacheRepository
import kt.school.starlord.domain.repository.product.ProductsRepository

/**
 * Repository that contains fake (mocked) data from all application data sources.
 */
/* ktlint-disable */
class MockRepository : CategoriesCacheRepository,
    SubcategoriesRepository,
    CategoriesWithSubcategoriesRepository,
    ProductsRepository,
    ProductsCacheRepository {

    val products = listOf(
        Product(
            22175010,
            "Куплю компактный диван",
            "Куплю компактный диван. Например, как Ikea Свэнста, но более надежный и не разъезжающийся. Можно не раскладывающийся. Ваша доставка в район метро Партизанская.",
            ProductType.BUY,
            "Минск",
            "file:///android_asset/products/1.jpg",
            ProductOwner("xrystal", 1524248),
            ProductPrice(150.0, false),
            1,
            1,
            true
        ),
        Product(
            22684921,
            "Диван-кровать Лагуна",
            "Продаем в связи с переездом. 8029593****. Самовывоз с ул.Кольцова.",
            ProductType.SELL,
            "Минск",
            "file:///android_asset/products/2.jpeg",
            ProductOwner("angel_1988", 1813103),
            ProductPrice(0.0, false),
            2,
            4
        ),
        Product(
            23028259,
            "Кресло-качалка из ротанга",
            "Обмен кресла-качалки из ротанга. Состояние нового. 8-029-113-**-** Лена",
            ProductType.EXCHANGE,
            "Минск",
            "file:///android_asset/products/3.jpg",
            ProductOwner("angel_1988", 1813103),
            ProductPrice(200.0, true),
            3,
            0
        ),
        Product(
            23082929,
            "Покос травы, стрижка газона",
            "Покос травы, бурьяна, стрижка газона. Быстро и качественно. Покос травы от 5р. Стрижка от 4р. Примеры работ:",
            ProductType.SERVICE,
            "Минск",
            "file:///android_asset/products/4.jpg",
            ProductOwner("BMW888IK5", 717419),
            ProductPrice(null, false),
            3,
            2
        ),
        Product(
            22917475,
            "iPhone 5S 16Gb Grey сост отл, не рев, хор комплект",
            "iPhone 5S 16Gb Grey сост отличное, без Touch Id, не рев. Телефон сзади под пленкой, спереди ни единой царапины, батарея держит отлично. В комплекте отдаю оригинальный кубик м переходником, ориг провод и ориг наушники. Из облака вышел. 8-025-934-**-**",
            ProductType.RENT,
            "Минск",
            "file:///android_asset/products/5.webp",
            ProductOwner("greendors", 76882),
            ProductPrice(160.0, false),
            4,
            1
        ),
        Product(
            22917475,
            "Офисное кресло",
            "Продам офисное кожаное кресло. б.у. Неполадки с механизмом качается назад-вперёд, влево-вправо - без фиксации. Нужно разбираться с механизмом! 8029-566-**-** (МТС)",
            ProductType.CLOSED,
            "Минск",
            "file:///android_asset/products/6.jpg",
            ProductOwner("Ta6aK", 442157),
            ProductPrice(30.0, false),
            5,
            0
        )
    )

    override fun getCategoriesLiveData(): LiveData<List<Category>> = MutableLiveData(
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

    override fun getSubcategories(categoryName: String) = MutableLiveData(
        listOf(
            Subcategory(
                "Шкафы. Комоды. Горки. Секции. Полки",
                categoryName,
                3388,
                ""
            ),
            Subcategory("Диваны. Кресла. Мягкая мебель", categoryName, 8962, ""),
            Subcategory("Столы. Стулья. Тумбы", categoryName, 2575, ""),
            Subcategory(
                "Кровати. Матрасы. Мебель для спальни",
                categoryName,
                2740,
                ""
            ),
            Subcategory("Кухни и кухонная мебель", categoryName, 2976, ""),
            Subcategory("Мебель для детской комнаты", categoryName, 4059, ""),
            Subcategory("Мебель для ванной", categoryName, 1904, ""),
            Subcategory("Офисная мебель", categoryName, 4704, ""),
            Subcategory("Элементы интерьера. Дизайн.", categoryName, 17044, ""),
            Subcategory(
                "Постельное белье и принадлежности",
                categoryName,
                11112,
                ""
            ),
            Subcategory(
                "Посуда и кухонные принадлежности",
                categoryName,
                638,
                ""
            ),
            Subcategory("Бытовая техника.", categoryName, 2842, ""),
            Subcategory(
                "Бытовая техника: ремонт, подключение и другие услуги",
                categoryName,
                423,
                ""
            )
        )
    )

    override suspend fun updateSubcategories(subcategories: List<Subcategory>) {}

    override suspend fun getCategoriesWithSubcategories() =
        CategoriesWithSubcategories(emptyList(), emptyList())

    override fun getProductsLiveData(subcategoryName: String): DataSource.Factory<Int, Product> {
        TODO()
    }

    override suspend fun updateProducts(subcategoryName: String, products: List<Product>) {}

    override suspend fun getProducts(link: String): List<Product> = products
}
