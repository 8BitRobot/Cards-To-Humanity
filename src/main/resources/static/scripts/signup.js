const app = new Vue(
    {
        el: "#vue_div",
        data: {
            username: "",
            display_name: "",
            password: "",
            password_retyped: "",
            email: "",
            error_message: "",
            signupButtonDisabled: false
        },
        methods: {
            signupButtonClicked: function() {
                var self = this;

                apiCreateUser(this.username, this.display_name, this.password, this.email)
                    .then(function() {
                        console.log("Success.");
                        window.location.href = "/login.html";
                    })
                    .catch(function(error_message) {
                        console.log("Failure.");
                        self.error_message = error_message;
                    });
            },
            checkPasswordsMatch: function() {
                if (this.password !== this.password_retyped) {
                    this.error_message = "Passwords do not match.";
                }
                else {
                    this.error_message = "";
                }
            }
        }
    }
);