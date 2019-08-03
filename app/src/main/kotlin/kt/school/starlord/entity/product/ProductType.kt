package kt.school.starlord.entity.product

import androidx.annotation.ColorRes
import kt.school.starlord.R

/**
 * Product type can be sell/buy/exchange/service/rent/closed.
 */
enum class ProductType(@ColorRes val color: Int) {
    SELL(R.color.colorBrightRed),
    BUY(R.color.colorGreen),
    EXCHANGE(R.color.colorPurple),
    SERVICE(R.color.colorBlue),
    RENT(R.color.colorOrange),
    CLOSED(R.color.colorGray);
}
