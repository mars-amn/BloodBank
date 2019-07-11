package abdullah.elamien.bloodbank.models

class AuthModel {
    var name: String? = null
    var email: String? = null
    var password: String? = null

    constructor(name: String, email: String, password: String) {
        this.name = name
        this.email = email
        this.password = password
    }

    constructor(email: String, password: String) {
        this.email = email
        this.password = password
    }
}
