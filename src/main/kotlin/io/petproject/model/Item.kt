package io.petproject.model

data class Item(val product: Product, var quantity: Int) {

    val subtotal = product.price.times(quantity.toBigDecimal())

    init {
        require(quantity > 0) { "Quantity must be greaterThan 0" }
    }

    fun addMore(quantity: Int) = apply {
        require(quantity > 0) { "Quantity must be greaterThan 0" }
        this.quantity += quantity
    }

    fun updateTo(quantity: Int) = apply {
        require(quantity >= 0) { "Quantity must be equalTo or greaterThan 0" }
        this.quantity = quantity
    }

}
