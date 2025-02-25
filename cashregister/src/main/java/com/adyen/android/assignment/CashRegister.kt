package com.adyen.android.assignment

import com.adyen.android.assignment.money.Change

/**
 * The CashRegister class holds the logic for performing transactions.
 * @param change The change that the CashRegister is holding.
 */
class CashRegister(private val change: Change) {
    /**
     * Performs a transaction for a product/products with a certain price and a given amount.
     * @param price The price of the product(s).
     * @param amountPaid The amount paid by the shopper.
     * @return The change for the transaction.
     * @throws TransactionException If the transaction cannot be performed.
     */
    fun performTransaction(price: Long, amountPaid: Change): Change {
        val totalAmountPaid = amountPaid.total

        //If total amount is less than the price, throw an exception
        if (totalAmountPaid < price) {
            throw TransactionException("Insufficient funds provided!")
        }

        //If total amount is equal to the price, return no change
        if (totalAmountPaid == price) {
            amountPaid.getElements().forEach { element ->
                change.add(element, amountPaid.getCount(element))
            }
            return Change.none()
        }

        val changeNeeded = totalAmountPaid - price

        val changeToGive = getChange(changeNeeded)

        amountPaid.getElements().forEach { element ->
            change.add(element, amountPaid.getCount(element))
        }
        changeToGive.getElements().forEach { element ->
            change.remove(element, changeToGive.getCount(element))
        }

        return changeToGive
    }

    /**
     * This method calculates the best way to give change using available bills/coins.
     * @param amountNeeded The total amount that needs to be returned as change.
     * @return A Change object representing the change to return.
     * @throws TransactionException If exact change cannot be given.
     */
    private fun getChange(amountNeeded: Long): Change {
        var remainingAmount = amountNeeded
        val finalChangeToGive = Change()

        val availableCashSorted = change.getElements()
            .sortedByDescending { it.minorValue } // sort in descending order for minimal change to give

        availableCashSorted.forEach { cashUnit ->
            if (remainingAmount == 0L) return@forEach // Exit early if exact change is given

            val unitValue = cashUnit.minorValue
            val totalAvailableUnits = change.getCount(cashUnit)
            val maxBillsOrCoinsNeeded = (remainingAmount / unitValue).toInt()
            val countOfBillsOrCoinsToUse = minOf(maxBillsOrCoinsNeeded, totalAvailableUnits)

            if (countOfBillsOrCoinsToUse > 0) {
                finalChangeToGive.add(cashUnit, countOfBillsOrCoinsToUse)
                val totalDeducted = countOfBillsOrCoinsToUse * unitValue
                remainingAmount -= totalDeducted
            }
        }

        // If exact change cannot be provided, throw an exception
        if (remainingAmount > 0) {
            throw TransactionException("Cannot provide exact change with available money.")
        }

        return finalChangeToGive
    }

    class TransactionException(message: String, cause: Throwable? = null) :
        Exception(message, cause)
}
