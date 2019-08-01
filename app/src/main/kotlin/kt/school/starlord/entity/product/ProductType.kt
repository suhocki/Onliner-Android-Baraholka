package kt.school.starlord.entity.product

import androidx.annotation.StringRes
import kt.school.starlord.R

/**
 * Product type can be sell/buy/exchange/service/rent/closed.
 */
enum class ProductType {
    SELL {
        override val color = R.color.colorBrightRed
    },
    BUY {
        override val color = R.color.colorGreen
    },
    EXCHANGE {
        override val color = R.color.colorPurple
    },
    SERVICE {
        override val color = R.color.colorBlue
    },
    RENT {
        override val color = R.color.colorOrange
    },
    CLOSED {
        override val color = R.color.colorGray
    };

    @get:StringRes
    abstract val color: Int
}
