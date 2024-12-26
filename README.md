# Kütüphane Yönetim Sistemi

Bu proje, bir kütüphane yönetim sistemi uygulamasıdır. Kullanıcılar, kitaplar ve işlemler (kitap ödünç alma, iade vb.) üzerine odaklanan modern bir yazılım mimarisi sunar. Uygulama, Java kullanılarak geliştirilmiş olup MySQL veritabanı ile çalışır.

## Özellikler

- **Kitap Yönetimi**: Kitap ekleme, silme, güncelleme ve listeleme.
- **Kullanıcı Yönetimi**: Kullanıcı kayıtlarını tutma ve düzenleme.
- **İşlem Yönetimi**: Kitap ödünç alma ve iade işlemlerini kayıt altına alma.

## Tasarım Desenleri

- **Decorator**: Dinamik olarak nesnelere yeni davranışlar ekleme.
- **Strategy**: Farklı algoritmaları çalıştırma.
- **Singleton**: Tüm uygulama boyunca bir nesnenin tek bir örneğinin olmasını sağlama.
- **Factory**: Nesne oluşturmayı merkezi bir hale getirme.
- **Observer**: Bir nesnede meydana gelen değişikliklerin diğer nesnelere bildirilmesi.
- **Command**: İşlemleri nesne haline getirip esnek ve geri alınabilir hale getirme.
- **Modüler**: Model-View-Controller (MVC) mimarisi ile organize edilmiş yapı.

## Mimari

Proje, modern yazılım prensiplerine uygun olarak organize edilmiştir. Kaynak kodlar `src` dizini altında aşağıdaki klasörlere ayrılmıştır:

- **Controller**: Veri akışını ve iş mantığını yönetir.
- **Decorator**: Nesnelere dinamik davranış ekleyen tasarım desenlerini içerir.
- **Model**: Veritabanı yapılarını ve veri modellerini tanımlar.
- **Strategy**: Alternatif algoritmaları uygulamak için kullanılır.
- **View**: Kullanıcı arayüzü bileşenlerini içerir.

## Gereksinimler

- **Java** 11 veya üzeri
- **MySQL** 8.0 veya üzeri
- **MySQL Connector** (`mysql-connector-j-9.1.0.jar` projeye dahil edilmiştir)
- Bir IDE (IntelliJ IDEA, Eclipse vb.)

## Kurulum

1. **Projeyi Klonlayın:**
   ```bash
   git clone https://github.com/kullaniciadi/kutuphane-sistemi.git
   ```

2. **Veritabanını Ayarlayın:**
   `kutuphane (3).sql` dosyasını MySQL yöneticinize aktarın:
   ```sql
   source /dosya/yolu/kutuphane (3).sql;
   ```

3. **Proje Bağlantılarını Yapılandırın:**
   `mysql-connector-j-9.1.0.jar` dosyasını proje bağlantılarına dahil edin.

4. **Projeyi Derleyip Çalıştırın:**
   - IDE kullanarak projeyi açın.
   - Ana sınıfı (Main.java) çalıştırın.

## Kullanım

### Personel Girişi

Personel kullanıcıları aşağıdaki işlemleri gerçekleştirebilir:

- **Kitap Ekle:** Yeni bir kitabı sisteme ekleyebilir.
- **Kitap Sil:** Mevcut bir kitabı sistemden kaldırabilir.
- **Kitap Güncelle:** Var olan bir kitabın bilgilerini güncelleyebilir.
- **Popüler Kitapları Göster:** En çok okunan kitapları listeleyebilir.
- **Çıkış Yap:** Sistemden güvenli bir şekilde çıkış yapabilir.

### Öğrenci Girişi

Öğrenci kullanıcıları aşağıdaki işlemleri gerçekleştirebilir:

- **Kitap Ödünç Al:** Sistemde mevcut kitaplardan birini ödünç alabilir.
- **Kitap İade Et:** Ödünç alınan kitabı iade edebilir.
- **Kitap Ara:** Sistemdeki kitapları arayabilir.
- **Popüler Kitapları Listele:** En popüler kitapları görebilir.
- **Kayıp Kitap Bildir:** Kayıp bir kitabı sisteme bildirebilir.
- **Kitapları Sırala:** Kitapları ada veya yayın tarihine göre sıralayabilir.

## Katkıda Bulunanlar

- **Adınız Soyadınız:** [GitHub Profiliniz](#) - Proje geliştiricisi
- **Arkadaşınızın Adı Soyadı:** [GitHub Profili](#) - Proje katkıcısı
