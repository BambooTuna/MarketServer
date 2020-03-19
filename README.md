# MarketServer


[Front](https://github.com/BambooTuna/market-front)


### SignUp, SignIn
```bash
$ curl -X POST -H "Content-Type: application/json" -d '{"mail":"bambootuna@gmail.com","pass":"pass"}' localhost:8080/signup -i
$ curl -X POST -H "Content-Type: application/json" -d '{"mail":"bambootuna@gmail.com","pass":"pass"}' localhost:8080/signin -i

$ export SESSION_TOKEN=~~~
```
HeaderName=`Set-Authorization`にセッショントークンがセットされた状態でレスポンスが帰ってくるのでそれを使う

### アカウント有効化メール送信
```bash
$ curl -X PUT -H "Authorization: $SESSION_TOKEN" localhost:8080/activate -i
```
### HealthCheck
セッショントークンが有効か確認する
HeaderName=`Authorization`にセッショントークンをセットする
```bash
$ curl -X GET localhost:8080/health -H "Authorization: $SESSION_TOKEN"
```

### LogOut
```bash
$ curl -X DELETE localhost:8080/logout -H "Authorization: $SESSION_TOKEN"
```

### パスワード初期化
```bash
$ curl -X POST -H "Content-Type: application/json" -d '{"mail":"bambootuna@gmail.com"}' localhost:8080/init -i
```

### SNS連携
- Line連携のリンク発行
```bash
$ curl -X POST http://localhost:8080/oauth2/signin/line
{"redirectUri":"~~~"}
```

- 連携承認後
`http://localhost:8080/oauth2/signin/line`にリダイレクト
HeaderName=`Set-Authorization`にセッショントークンがセットされる


### 全ての出品
```bash
$ curl -X GET "localhost:8080/products?limit=5&page=1"
```

### 出品詳細
```bash
$ curl -X GET "localhost:8080/product/:product_id?limit=5&page=1"
```

### 自分の出品
```bash
$ curl -X GET -H "Authorization: $SESSION_TOKEN" "localhost:8080/products/self?limit=5&page=1&states=open,draft"
```

### 出品する
```bash
$ curl -X POST localhost:8080/product -H "Authorization: $SESSION_TOKEN" -H "Content-Type: application/json" -d '{"title":"タイトル","detail":"商品詳細","price":1000,"state":"open"}'
```

### 出品編集
```bash
$ curl -X PUT localhost:8080/product/:product_id -H "Authorization: $SESSION_TOKEN" -H "Content-Type: application/json" -d '{"title":"タイトル","detail":"商品詳細","price":1100,"state":"draft"}'
```
