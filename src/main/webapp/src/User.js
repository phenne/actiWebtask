class User {

    constructor(firstName, lastName, userName, password, isManager) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.manager = isManager;
        this.id = null;
    }

    set userId(id) {
        this.id = id;
    }
}
