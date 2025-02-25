package com.adyen.android.assignment

import com.adyen.android.assignment.money.Bill
import com.adyen.android.assignment.money.Change
import com.adyen.android.assignment.money.Coin
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class CashRegisterTest {

    @Test
    fun testSuccessfulPurchaseWithChange() {
        val register = CashRegister(
            Change()
                .add(Bill.TWENTY_EURO, 2)
                .add(Bill.TEN_EURO, 2)
                .add(Coin.ONE_EURO, 5)
        )

        val moneyGivenByCustomer = Change()
            .add(Bill.FIFTY_EURO, 1)

        val expectedChangeToCustomer = Change()
            .add(Bill.TEN_EURO, 1)
            .add(Coin.ONE_EURO, 3)

        val actualChange = register.performTransaction(37_00, moneyGivenByCustomer)
        assertEquals(expectedChangeToCustomer, actualChange)
    }

    @Test
    fun testExactPaymentNoChangeNeeded() {
        val register = CashRegister(
            Change()
                .add(Bill.FIFTY_EURO, 2)
                .add(Coin.TEN_CENT, 5)
        )

        val moneyGivenByCustomer = Change()
            .add(Bill.TWENTY_EURO, 1)

        val expectedChangeToCustomer = Change.none()
        val actualChange = register.performTransaction(20_00, moneyGivenByCustomer)

        assertEquals(expectedChangeToCustomer, actualChange)
    }

    @Test
    fun testPurchaseFailsDueToInsufficientPayment() {
        val register = CashRegister(
            Change()
                .add(Bill.TWENTY_EURO, 2)
        )

        val moneyGivenByCustomer = Change()
            .add(Bill.TEN_EURO, 1)

        try {
            register.performTransaction(15_00, moneyGivenByCustomer)
            fail("Expected TransactionException because customer didn't pay enough.")
        } catch (e: CashRegister.TransactionException) {
            // Some Exception
        }
    }

    @Test
    fun testPurchaseFailsDueToLackOfExactChange() {
        val register = CashRegister(
            Change()
                .add(Bill.TWENTY_EURO, 1)
                .add(Coin.TWO_EURO, 1)
        )

        val moneyGivenByCustomer = Change()
            .add(Bill.FIFTY_EURO, 1)

        try {
            register.performTransaction(27_00, moneyGivenByCustomer)
            fail("Expected TransactionException because the register does not have exact change.")
        } catch (e: CashRegister.TransactionException) {
            // Some Exception
        }
    }
}

