Stock watchlist app with basic CRUD features.

# API Doc

http://localhost:8080/api/v1/swagger-ui/index.html

# Assumptions

1) Watchlist cannot exist without an owner.
2) No concept of currency, instead decimals are used for simplicity.
3) Symbols are unique for stocks.

# Limitations

1) Single user application.
2) No auth n/z.
3) No unit or integration tests.
4) Error handling mechanism is catch and ignore.
5) Searching a stock is unified
   i.e. we check if search string is either present in stock name or symbol.
6) No default path mapping if request path doesn't match.
7) API-level access to Yahoo Finance quotes data has been disabled.
   See resources/static/images/yahoo-finance-disabled.png for more details.

   Using free quotes service offered by alpha vantage (but only 25 requests per day are allowed,
   so we have disabled quotes in application.properties).

