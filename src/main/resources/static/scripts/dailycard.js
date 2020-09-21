let dailycard = new Vue(
    {
        el: "footer",
        data: function () {
            let noShowFooter;
            try {
                let noShowFooterCookie = document.cookie
                    .split(';')
                    .find((item) => item.trim().startsWith('noAskDailyCard='))
                    .split("=")[1];
                noShowFooter = noShowFooterCookie === "true";
            } catch (TypeError) {
                noShowFooter = false;
            }
            return {
                showFooter: !noShowFooter,
            }
        },
        methods: {
            redirectToDailyPage: function () {
                window.location.href = "/daily_card.html";
            },
            neverAskAgain: function () {
                document.cookie = "noAskDailyCard=true"
                this.showFooter = false;
            },
            closeForNow: function () {
                this.showFooter = false;
            }
        }
    }
)