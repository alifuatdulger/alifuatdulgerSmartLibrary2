# alifuatdulgerSmartLibrary2
Piri Reis Üniversitesi NESNEYE DAYALI PROGRAMLAMA-II dersi,Java dilinde yazılmış ,SmartLibrary2 projesidir.Ali Fuat Dülger tarafından hazırlanmıştır.


SmartLibraryPlus - ORM Tabanlı Akıllı Kütüphane Sistemi
Bu proje, bir üniversite kütüphanesinin yönetimini modern Hibernate ORM teknolojisi kullanarak otomatize eden bir masaüstü konsol uygulamasıdır. Klasik JDBC yaklaşımlarından farklı olarak, veritabanı işlemleri tamamen nesne yönelimli bir yapıda kurgulanmıştır.

Kullanılan Teknolojiler
Dil: Java 17+
ORM: Hibernate 6.4.1.Final
Veritabanı: SQLite
Yönetim: Maven (Bağımlılık ve Build yönetimi)

Proje Mimarisi (Zorunlu Yapı)
Proje, katmanlı mimari prensiplerine uygun olarak 4 ana pakete ayrılmıştır:

entity: Veritabanı tablolarını temsil eden POJO sınıfları ve JPA anatasyonları.

dao (Data Access Object): Veritabanı CRUD (Create, Read, Update, Delete) operasyonlarının yönetildiği katman.

util: Hibernate SessionFactory yapılandırmasını sağlayan yardımcı sınıflar.

app: Kullanıcı etkileşiminin sağlandığı konsol menüsü ve ana döngü.

Veritabanı İlişkileri (Entity Mapping)
Projede Hibernate anatasyonları kullanılarak aşağıdaki ilişkiler kurulmuştur:

Student - Loan (One-to-Many): Bir öğrenci birden fazla kitap ödünç alabilir.

Loan - Book (One-to-One): Her ödünç alma kaydı tek bir spesifik kitapla eşleşir.

Otomatik Tablo Oluşturma: hbm2ddl.auto=update ayarı sayesinde tablolar Java sınıflarından otomatik olarak üretilir.

Öne Çıkan Özellikler
Nesne Odaklı CRUD: SQL sorgusu yazmadan Hibernate Session metotları (persist, merge, get) ile veri yönetimi.

İş Kuralları Kontrolü: Bir kitap zaten ödünç alınmışsa (BORROWED), sistem aynı kitabın tekrar ödünç verilmesini engeller.

Durum Yönetimi: Kitap iade edildiğinde kitap durumu otomatik olarak AVAILABLE (Mevcut) hale gelir ve iade tarihi kaydedilir.

Transaction Yönetimi: Veri bütünlüğünü korumak adına tüm yazma işlemleri Hibernate Transaction blokları içinde gerçekleştirilmiştir.

Kullanım Talimatları
-Projeyi bir IDE ile açın.

-Maven bağımlılıklarının yüklendiğinden emin olun.

-src/main/java/app/Main.java dosyasını çalıştırın.

-Konsol üzerinden sunulan menüdeki sayıları kullanarak işlemleri gerçekleştirin.
