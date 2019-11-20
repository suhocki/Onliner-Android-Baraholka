package kt.school.starlord.domain.entity.product

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import kt.school.starlord.R

/**
 * Product type can be sell/buy/exchange/service/rent/closed.
 */
enum class ProductType(@StringRes val stringRes: Int, @ColorRes val colorRes: Int) {
    SELL(R.string.sell, R.color.colorBrightRed),
    BUY(R.string.buy, R.color.colorGreen),
    EXCHANGE(R.string.exchange, R.color.colorPurple),
    SERVICE(R.string.service, R.color.colorBlue),
    RENT(R.string.rent, R.color.colorOrange),
    CLOSED(R.string.closed, R.color.colorGray),
    WARNING(R.string.warning, R.color.colorBurgundy);
}
