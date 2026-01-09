package com.example.britishcoffee.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.britishcoffee.models.CartItem
import com.example.britishcoffee.models.CoffeeItem
import com.example.britishcoffee.models.DataProvider
import com.example.britishcoffee.models.Order
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class UserSession(val name: String, val email: String)
data class UserAccount(val name: String, val password: String)

class OrderViewModel : ViewModel() {

    private val _registeredUsers = mutableMapOf<String, UserAccount>()
    var currentUser by mutableStateOf<UserSession?>(null)
        private set

    fun registerUser(name: String, email: String, password: String): Boolean {
        if (_registeredUsers.containsKey(email)) return false
        _registeredUsers[email] = UserAccount(name = name, password = password)
        return true
    }

    fun loginUser(email: String, password: String): Boolean {
        val account = _registeredUsers[email]
        if (account != null && account.password == password) {
            currentUser = UserSession(name = account.name, email = email)
            return true
        }
        return false
    }

    fun logout() { currentUser = null }

    private val _cartItems = mutableStateListOf<CartItem>()
    val cartItems: List<CartItem> get() = _cartItems

    fun addToCart(coffee: CoffeeItem, size: String, quantityToAdd: Int = 1, isPromo: Boolean = false) {
        val existing = _cartItems.find { it.coffee.id == coffee.id && it.size == size && it.isPromo == isPromo }
        if (existing != null) {
            existing.quantity.value += quantityToAdd
        } else {
            _cartItems.add(CartItem(coffee, mutableStateOf(quantityToAdd), size, isPromo))
        }
    }

    fun removeFromCart(item: CartItem) {
        _cartItems.remove(item)
    }

    fun incrementQuantity(item: CartItem) {
        item.quantity.value++
    }

    fun decrementQuantity(item: CartItem) {
        if (item.quantity.value > 1) {
            item.quantity.value--
        } else {
            removeFromCart(item)
        }
    }

    fun calculateTotal(): Double {
        var total = 0.0
        for (item in _cartItems) {
            val qty = item.quantity.value.toDouble()
            val price = item.coffee.price
            total += if (item.isPromo) (price * 0.5) * qty else price * qty
        }
        return total
    }

    private val _wishlistItems = mutableStateListOf<CoffeeItem>()
    val wishlistItems: List<CoffeeItem> get() = _wishlistItems

    fun isFavorite(coffee: CoffeeItem): Boolean = _wishlistItems.any { it.id == coffee.id }

    fun toggleFavorite(coffee: CoffeeItem) {
        if (isFavorite(coffee)) {
            _wishlistItems.removeIf { it.id == coffee.id }
        } else {
            _wishlistItems.add(coffee)
        }
    }

    private val _orders = mutableStateListOf<Order>()
    val orders: List<Order> get() = _orders

    fun placeOrder(totalAmount: Double) {
        if (_cartItems.isNotEmpty()) {
            val newOrder = Order(
                id = "#${(10000..99999).random()}",
                items = _cartItems.toList(),
                totalAmount = totalAmount,
                date = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(Date()),
                status = "Processing"
            )
            _orders.add(0, newOrder)
            _cartItems.clear()
        }
    }
}
