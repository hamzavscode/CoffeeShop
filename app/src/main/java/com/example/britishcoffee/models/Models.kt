package com.example.britishcoffee.models

import androidx.annotation.DrawableRes
import androidx.compose.runtime.MutableState
import com.example.britishcoffee.R

data class CoffeeItem(
    val id: Int,
    val name: String,
    val description: String,
    val shortDesc: String,
    val price: Double,
    @DrawableRes val imageRes: Int,
    val rating: Double = 4.8,
    val reviews: Int = 230
)

data class CartItem(
    val coffee: CoffeeItem,
    val quantity: MutableState<Int>,
    val size: String,
    val isPromo: Boolean = false
)

data class Order(
    val id: String,
    val items: List<CartItem>,
    val totalAmount: Double,
    val date: String,
    val status: String
)

object DataProvider {
    val coffeeList = listOf(
        CoffeeItem(1, "Cappuccino", "A classic Italian coffee drink that is traditionally prepared with equal parts double espresso, steamed milk, and steamed milk foam on top.", "With Oat Milk", 4.20, R.drawable.cappoccino),
        CoffeeItem(2, "Espresso", "A full-flavored, concentrated form of coffee that is served in shots. It is made by forcing pressurized hot water through very finely ground coffee beans.", "With Chocolate", 3.50, R.drawable.espersso),
        CoffeeItem(3, "Cortado", "An espresso mixed with a roughly equal amount of warm milk to reduce the acidity. The milk in a cortado is steamed, but not frothy.", "Spanish Style", 4.00, R.drawable.cortado),
        CoffeeItem(4, "Flat White", "A coffee drink consisting of espresso with microfoam (steamed milk with small, fine bubbles and a glossy or velvety consistency).", "Smooth texture", 3.20, R.drawable.flat_white),
        CoffeeItem(5, "Americano", "A type of coffee drink prepared by diluting an espresso with hot water, giving it a similar strength to, but different flavor from, traditionally brewed coffee.", "Rich & Dark", 3.80, R.drawable.americano),
        CoffeeItem(6, "Café Crème", "A rich coffee similar to a latte but often made with heavy cream or a mix of milk and cream for a much smoother experience.", "Extra Creamy", 4.50, R.drawable.cafe_creme),
        CoffeeItem(7, "Turkish Coffee", "Very finely ground coffee beans are brewed by boiling. It is unfiltered and served in a small cup where the grounds are allowed to settle.", "Traditional", 3.00, R.drawable.coffee_turc),
        CoffeeItem(8, "Iced Latte", "A chilled version of the classic latte, served over ice with a double shot of espresso and cold milk.", "Cold & Refreshing", 4.50, R.drawable.latte_glace),
        CoffeeItem(9, "Macchiato", "An espresso coffee drink with a small amount of milk, usually foamed. In Italian, macchiato means 'stained' or 'spotted'.", "Stained Espresso", 3.70, R.drawable.macchiato),
        CoffeeItem(10, "Premium Cappuccino", "Our signature cappuccino made with premium Arabica beans and topped with a dusting of fine Belgian cocoa.", "Premium Choice", 5.20, R.drawable.premium_cappuccino),
        CoffeeItem(11, "Pumpkin Latte", "A seasonal favorite combining our signature espresso with autumnal spices, pumpkin flavor, and steamed milk.", "Seasonal Special", 5.50, R.drawable.pumpkin_latte),
        CoffeeItem(12, "Herbal Tea", "A soothing Queen Berry herbal infusion, naturally caffeine-free and bursting with forest berry flavors.", "Fruit Infusion", 3.20, R.drawable.queen_berry_herbal_tea)
    )
}

