# Random Quote Generator — Android App (CodeAlpha Task 2)

Ye ek simple native Android app hai jo Kotlin mein bani hai. Har button click par
naya random quote dikhata hai, "Share" se quote WhatsApp/Instagram waghera pe
share ho sakta hai.

## Features (jo task mein maanga gaya tha)
- App khulte hi random quote show hota hai.
- "New Quote" button — click karne par naya (different) quote.
- Har quote ka text aur author dono clearly dikhte hain.
- Clean, minimal, card-style UI.

## Quotes kabhi repeat nahi hongi
20 hardcoded quotes ki jagah, app live **dummyjson.com/quotes/random** API se
quotes fetch karta hai — is database mein 1450+ quotes hain. App har dikhayi
gayi quote ka ID yaad rakhta hai (`shownIds` set), aur jab tak app open hai,
wahi quote dobara nahi dikhayega. Agar internet na ho, app automatically ek
chhoti si offline list (10 quotes) par switch ho jata hai — wahan bhi same
"no repeat until pool khatam" logic follow hoti hai. Status text (chhota
sa "● Fresh quotes online" / "● Offline") screen ke top par dikhta hai
taake pata chale kaunsa source use ho raha hai.

Internet permission (`AndroidManifest.xml`) is liye add ki gayi hai.

## Dark mode fix
App ka apna fixed branded look hai (purple gradient + white card), isliye
theme ko `DayNight` se hata kar plain `MaterialComponents` par set kiya gaya
hai, aur manifest mein `android:forceDarkAllowed="false"` add kiya gaya hai.
Isse Android ka automatic "Force Dark" system feature humari custom colors
ko invert/adjust nahi karega — app hamesha same, intended design mein dikhega
chahe phone system dark mode mein ho ya light mode mein.

## Naya app icon
Placeholder system icon (`ic_dialog_info`) hata kar ek custom quote-mark logo
banaya gaya hai — purple-to-indigo gradient background par safed aur gold
color ke do quotation-mark commas, jo app ki theme se match karta hai. Modern
(Android 8+) phones par ye adaptive icon ke tor par show hoga (launcher apne
hisaab se shape mask kar dega — circle, squircle, waghera), aur purane phones
par bhi automatically fallback version dikhega.

## Android Studio mein kaise chalayein
1. Android Studio open karein → **Open** → is `RandomQuoteGenerator` folder ko select karein.
2. Gradle sync hone dein (pehli baar thoda time lagega, internet chahiye).
3. Top-right mein Run (▶) button dabayein, ya emulator/phone connect karke run karein.
4. App launch hoga aur foran ek random quote dikhayega.

## Project Structure
```
RandomQuoteGenerator/
├── app/
│   ├── build.gradle
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/codealpha/randomquotegenerator/MainActivity.kt
│       └── res/
│           ├── layout/activity_main.xml
│           ├── values/ (strings, colors, themes)
│           └── drawable/ (gradient background, card, button)
├── build.gradle
├── settings.gradle
└── gradle.properties
```

## Quotes list customize karna
`MainActivity.kt` mein `quotes` list ke andar apne quotes add/edit/remove kar
sakte hain — har entry `Quote("text", "author")` format mein hai.

## GitHub par upload (CodeAlpha instructions ke mutabiq)
Repo ka naam yeh rakhein:
```
CodeAlpha_RandomQuoteGenerator
```
Phir poora source code push kar dein aur LinkedIn par project ka video
explanation post karein, GitHub repo link ke saath, @CodeAlpha ko tag karte
hue.
