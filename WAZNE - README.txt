Aby m�c uruchomi� klienta (a dok�adnie socket kt�ry si� po��czy z serwerem) trzeba:
Doda� do manifesta wpis:
<uses-permission android:name="android.permission.INTERNET" />

Oraz nie mo�na tworzy� soketa w g��wnym w�tku. Aby go uruchomi� trzeba stworzy� jakas klase ASYNCHRONICZN� (AsynTask), w przeci��onej metodzie doInBackground stworzyc soketa i zaplementowa� komunikacj� oraz wywo�a� t� metod�: NIE poprzez jej nazwe(doInBackground), tylko poprzez metode execute() na obiekcie tej klasy
