package io.petproject.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ShoppingCartTest {
    private val shoppingCart = ShoppingCart()

    @BeforeEach
    fun `setup`() {
        val console = Product("PS4 Slim 1TB", ProductType.PHYSICAL, 1899.00)
        val chair = Product("PDP Chair", ProductType.PHYSICAL, 399.00)
        val netflix = Product("Netflix Familiar Plan", ProductType.SUBSCRIPTION, 29.90)
        val spotify = Product("Spotify Premium", ProductType.SUBSCRIPTION, 14.90)
        val amazon = Product("Amazon Prime", ProductType.SUBSCRIPTION, 12.90)
        val book = Product("Cracking the Code Interview", ProductType.PHYSICAL_TAX_FREE, 219.57)
        val anotherBook = Product("The Hitchhiker's Guide to the Galaxy", ProductType.PHYSICAL_TAX_FREE, 120.00)
        val musicDigitalAlbum = Product("Stairway to Heaven", ProductType.DIGITAL, 5.00)
        val videoGameDigitalCopy = Product("Nier:Automata", ProductType.DIGITAL, 129.90)

        shoppingCart.add(console, 1)
                .add(chair, 2)
                .add(book, 2)
                .add(anotherBook, 1)
                .add(musicDigitalAlbum, 1)
                .add(videoGameDigitalCopy, 4)
                .add(netflix, 1)
                .add(spotify, 1)
                .add(amazon, 1)
    }

    @Test
    fun `when adding a Product with quantity lowerThan or equalTo 0, throw IllegalArgEx`() {
        val ex = assertThrows(IllegalArgumentException::class.java) {
            shoppingCart.add(Product("product", ProductType.PHYSICAL, 1.90), 0)
        }
        assertThat(ex.message).isEqualTo("Quantity must be greaterThan 0")
    }

    @Test
    fun `when adding a Product that is already in the Cart, add up to the quantity`() {
        val videoGameDigitalCopy = Product("Nier:Automata", ProductType.DIGITAL, 129.90)
        shoppingCart.add(videoGameDigitalCopy, 10)
        assertThat(shoppingCart.items[videoGameDigitalCopy]!!.quantity).isEqualTo(14)
    }

    @Test
    fun `when updating Quantity of a Product to greaterThan Zero, overwrite the value`() {
        val videoGameDigitalCopy = Product("Nier:Automata", ProductType.DIGITAL, 129.90)
        shoppingCart.updateQuantity(videoGameDigitalCopy, 1)
        assertThat(shoppingCart.items[videoGameDigitalCopy]!!.quantity).isEqualTo(1)
    }

    @Test
    fun `when updating Quantity of a Product to Zero, it should delete it from the Cart`() {
        val videoGameDigitalCopy = Product("Nier:Automata", ProductType.DIGITAL, 129.90)
        shoppingCart.updateQuantity(videoGameDigitalCopy, 0)
        assertThat(shoppingCart.items.containsKey(videoGameDigitalCopy)).isFalse()
    }

    @Test
    fun `when updating Quantity of a Product to lowerThan Zero, it should throw IllegalArgEx`() {
        val videoGameDigitalCopy = Product("Nier:Automata", ProductType.DIGITAL, 129.90)
        val ex = assertThrows(IllegalArgumentException::class.java) {
            shoppingCart.updateQuantity(videoGameDigitalCopy, -1)
        }
        assertThat(ex.message).isEqualTo("Quantity must be equalTo or greaterThan 0")

    }
    @Test
    fun `when updating Quantity of a Product that is not in the cart, throw IllegalArgEx`() {
        val someProductNotInTheCart = Product("lorem ipsum", ProductType.PHYSICAL, 19.90)
        val ex = assertThrows(IllegalArgumentException::class.java) {
            shoppingCart.updateQuantity(someProductNotInTheCart, 4)
        }
        assertThat(ex.message).isEqualTo("Product specified is not in the Cart")
    }

    @Test
    fun `when deleting a Product that is in the cart, it should vanish`() {
        val videoGameDigitalCopy = Product("Nier:Automata", ProductType.DIGITAL, 129.90)
        shoppingCart.delete(videoGameDigitalCopy)
        assertThat(shoppingCart.items.containsKey(videoGameDigitalCopy)).isFalse()
    }

    @Test
    fun `when deleting a Product that is NOT in the cart, it should vanish`() {
        val someProductNotInTheCart = Product("lorem ipsum", ProductType.PHYSICAL, 19.90)
        val ex = assertThrows(IllegalArgumentException::class.java) {
            shoppingCart.delete(someProductNotInTheCart)
        }
        assertThat(ex.message).isEqualTo("Product specified is not in the Cart")
    }

    @Test
    fun `when computing Subtotal, sum the price of all items in the cart`() {
        assertThat(shoppingCart.subtotal().toPlainString()).isEqualTo("3838.44")
    }

    @Test
    fun `when computing Subtotal, return Zero if there's nothing in the cart`() {
        val anotherShoppingCart = ShoppingCart()
        assertThat(anotherShoppingCart.subtotal().toPlainString()).isEqualTo("0.00")
    }

    @Test
    fun `when checking out, build an Order for Physical, another for Digital, and another for each per Subscription`() {
        val account = Account(name = "John", email = "john.doe@domain.suffix")
        val orders = shoppingCart.checkout(account)
        assertThat(orders.size).isEqualTo(5)
    }

}