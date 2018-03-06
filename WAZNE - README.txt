Aby móc uruchomiæ klienta (a dok³adnie socket który siê po³¹czy z serwerem) trzeba:
Dodaæ do manifesta wpis:
<uses-permission android:name="android.permission.INTERNET" />

Oraz nie mo¿na tworzyæ soketa w g³ównym w¹tku. Aby go uruchomiæ trzeba stworzyæ jakas klase ASYNCHRONICZN¥ (AsynTask), w przeci¹¿onej metodzie doInBackground stworzyc soketa i zaplementowaæ komunikacjê oraz wywo³aæ t¹ metodê: NIE poprzez jej nazwe(doInBackground), tylko poprzez metode execute() na obiekcie tej klasy

Skonczylem wstepnie klienta. Projekt te¿ dodatkowo robie sam dla siebie wiec jezeli macie jakas inna koncepcje jak to ma wygladac to mozecie zupelnie od zera to napisac lub zmienic aktualny kod. Mo¿emy równie¿ oddaæ to co zrobi³em bo teoretycznie jest chyba wszystko co maækowski mówi³