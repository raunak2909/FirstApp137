package gov.rajasthan.firstapp137

data class ProductModel(
    var catId: String = "",
    var subCatId: String = "",
    var productId: String = "",
    var productTitle: String = "",
    var productDesc: String = "",
    var productQty: Int = 0,
    var productPrice: Double = 0.0,
    var productDiscountPer: Double = 0.0,
    var productImg: String = "",
    var productColors: String = "",
    var productUnit: String = "",
    var productImages: ArrayList<String> = arrayListOf(),
    var createdAt: Long = 0)
