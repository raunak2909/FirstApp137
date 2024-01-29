package gov.rajasthan.firstapp137.admin

data class UserModal(
    var dob: String? = "",
    var email: String? = "",
    var gender: String? = "",
    var name: String? = "",
    var pass: String? = "",
    var phnNo: String? = "",
    var profile_pic: String? = "",
    var status: Int? = -1,
) {
    var userId: String = ""
}
