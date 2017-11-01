class ValidationService {

    constructor(dataFields, passwordFields, isPasswordRequired) {
        this.dataFields = dataFields;
        this.passwordFields = passwordFields;
        this.isPasswordRequired = isPasswordRequired;
    }

    validate() {
        let isOk = true;

        for (let field of this.dataFields) {
            console.log(field);
            if (!field.value) {
                $(field.parentNode).addClass("has-error");
                isOk = false;
            } else {
                $(field.parentNode).removeClass("has-error");
            }
        }

        let password = this.passwordFields[0];
        let confirmPassword = this.passwordFields[1];

        return this.checkPass(password, confirmPassword, this.isPasswordRequired) && isOk;
    }

    checkPass(pass, confirmPass, isRequired) {
        let isOk = true;
        if (isRequired) {
            if (!pass.value || pass.value !== confirmPass.value) {
                isOk = false;
            }
        } else {
            if (pass.value) {
                if (pass.value !== confirmPass.value) {
                    isOk = false;
                }
            }
        }

        if (!isOk) {
            $(pass.parentNode).addClass("has-error");
            $(confirmPass.parentNode).addClass("has-error");
        } else {
            $(pass.parentNode).removeClass("has-error");
            $(confirmPass.parentNode).removeClass("has-error");
        }

        return isOk;
    }
}