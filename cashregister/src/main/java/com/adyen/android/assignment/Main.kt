package com.adyen.android.assignment

import com.adyen.android.assignment.money.Bill
import com.adyen.android.assignment.money.Change
import com.adyen.android.assignment.money.Coin

fun main() {
    // Amount available in the Cash Register
    val cashRegister = CashRegister(
        Change()
            .add(Bill.FIFTY_EURO, 2)
            .add(Bill.TWENTY_EURO, 3)
            .add(Bill.TEN_EURO, 5)
            .add(Coin.TWO_EURO, 4)
            .add(Coin.ONE_EURO, 10)
    )

    val testCases = listOf(
        Pair(
            37_00,  //Amount of Item
            Change().add(Bill.TWENTY_EURO, 1) // Money given by customer
        ),
        Pair(
            20_00,
            Change().add(Bill.TEN_EURO, 2)
        ),
        Pair(
            12_00,
            Change().add(Bill.TEN_EURO, 2)
        ),
        Pair(
            5_00,
            Change().add(Coin.ONE_EURO, 3)
        ),
    )

    testCases.forEachIndexed { index, (priceOfItem, amountGivenByCustomer) ->
        println("\nTest #${index + 1}: Item Price = €${priceOfItem / 100}, Paid = $amountGivenByCustomer")
        try {
            val changeReturned =
                cashRegister.performTransaction(priceOfItem.toLong(), amountGivenByCustomer)
            println("Success! Change given: $changeReturned")
        } catch (e: CashRegister.TransactionException) {
            println("Transaction Failed: ${e.message}")
        }
    }
}