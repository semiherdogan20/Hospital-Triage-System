# Hospital Triage System

Hastane acil servisinde gelen hastaları otomatik olarak önceliklendir ve uygun doktorlara ata.

## 🎯 Proje Amacı

Vücut ölçümleri (nabız, ateş, tansiyon) temelinde:
- Hastaların aciliyet puanını otomatik hesapla
- Bekleyen hastayı zaman içinde daha acil yap (Aging)
- En uygun doktoru ata
- Eş zamanlı işlemlerden korunma (Optimistic Locking)

## 🏗️ Tech Stack

Spring Boot 3.x | MySQL 8.0 | Spring Data JPA | Hibernate | Spring Scheduler

## 📋 Kurulum

### 1. Veritabanı
```sql
CREATE DATABASE triage_system;
```

### 2. application.properties
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/triage_system
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### 3. Çalıştır
```bash
mvn clean install
mvn spring-boot:run
```

Başlangıç kuralları otomatik yüklenir.

## 📊 Veritabanı Şeması

**Patients**: id, name, surname, tckNo, birthDate  
**Vitals**: id, pulse, systolicBp, diastolicBp, bodyTemp, visit_id  
**Visits**: id, patient_id, status, urgencyScore, createdAt, doctor_id, version  
**Doctors**: id, name, specialty, skillScore, dailyPatientCount  
**Rules**: id, conditionField, operator, value, scoreImpact, priority

## 🔄 İş Akışı

```
Hasta gelir → Vitals ölçülür → Kurallar uygulanır → 
Aciliyet puanı hesaplanır → WAITING durumunda kaydedilir →
AgingService dakikada puanı artırır → Doktor hastayı alır →
EXAMINATION → DISCHARGED
```

## 📡 Ana API Endpoints

**Yeni Hasta**
```http
POST /api/triage
{ "pulse": 110, "systolicBp": 160, "diastolicBp": 95, "bodyTemp": 38.5 }
```

**Sıradaki Hastayı Al**
```http
GET /api/queue/next/{doctorId}
```

**Durum Değiştir**
```http
POST /api/visit/{id}/status?newStatus=DISCHARGED
```

**Kural Ekle**
```http
POST /api/v1/rules
{ "conditionField": "pulse", "operator": ">", "value": "100", "scoreImpact": 15 }
```

**Kural Sil**
```http
DELETE /api/v1/rules/{ruleId}
```

## 🧠 Kural Motoru

Kurallar: `conditionField operator value → scoreImpact puan`

İzin verilen alanlar: `pulse`, `bodyTemp`, `systolicBp`, `diastolicBp`  
İzin verilen operatörler: `>`, `<`, `>=`, `<=`, `=`

Örnek:
- Nabız > 100 ise +10 puan
- Ateş > 38 ise +20 puan
- Büyük Tansiyon > 160 ise +15 puan

## 🔄 Durum Makinesi

```
WAITING → EXAMINATION → DISCHARGED
MANUAL_REVIEW → WAITING
```

Geçişler `VisitTransitionService` tarafından kontrol edilir.

## ⚡ Zamanla Öncelik Artışı (Aging)

`@Scheduled(fixedRate = 60000)` - Her 60 saniyede bir çalışır  
WAITING hastaların urgencyScore'unu +1 artırır. Batch processing ile 100'erli sayfalar halinde işlenir.

## 🔐 Eş Zamanlılık (Optimistic Locking)

Visit entitysinde `@Version` alanı var. İki doktor aynı hastayı almaya çalışırsa sistem onu engeller ve hata döndürür.

## 🎯 Doktor Eşleştirme

En uygun doktor:
```
finalScore = urgencyScore + waitingTime - skillGap - fatiguePenalty
```

En yüksek puanı alan doktor seçilir.

## ⚠️ Hata Yönetimi

Tüm hatalar `GlobalExceptionHandler` tarafından yakalanır.

```json
{
  "message": "Hata açıklaması",
  "status": 400,
  "timeStamp": 1704067200000
}
```

## 🛠️ Geliştirme Yol Haritası

- [ ] Frontend UI (React/Angular)
- [ ] SMS/Email bildirimleri
- [ ] Doktor performans analizi
- [ ] Hastanın bekleme süresi tahminlemesi
- [ ] JWT token-based authentication
- [ ] API Rate Limiting


