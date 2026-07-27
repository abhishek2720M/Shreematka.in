package com.example.data.model

data class PaymentSettings(
    val upiId: String = "shreematka@upi",
    val phonePeNumber: String = "9876543210",
    val gPayNumber: String = "9876543210",
    val paytmNumber: String = "9876543210",
    val bankAccountNo: String = "123456789012",
    val bankIfsc: String = "SBIN0001234",
    val bankHolderName: String = "SHREE MATKA OFFICIAL",
    val minDeposit: Int = 100,
    val minWithdraw: Int = 300,
    val isGPayEnabled: Boolean = true,
    val isPhonePeEnabled: Boolean = true,
    val isPaytmEnabled: Boolean = true,
    val isBhimQrEnabled: Boolean = true,
    val isBankTransferEnabled: Boolean = true,
    val whatsappSupportNumber: String = "919876543210"
)
