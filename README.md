# Image Viewer

This project can help user with viewing a picture if user has link for image. App has a convenient and intuitive interface. 

## Getting Started

At first, user should install both modules.

<img src="https://i.imgur.com/GnGa3Aq.png" width="250" />


[First module] is created to input URLs and to work with SQLite database.
[Second module] is created to show an image and to make changes in database of [module_A].

Attention! User always must run module_A first! Module_B is not independent program!

### Prerequisites

To install our project user should have device with API 21: Android 5.0 (Lollipop) or higher.

### Installing

Click [here](https://github.com) to download apk-files,

<img src="скрін з репозиторієм де вони лежать" width="250" />


and click [here](https://github.com/TrueDevels/Tabs/tree/Ivan_Branch) to download whole project.

<img src="https://i.imgur.com/7QWDUbW.png" />

### How to use it

User should input URL and press OK:

<img src="https://i.imgur.com/nPvHyE9.png" width="250" />


If everything is fine, user will see this:

<img src="https://i.imgur.com/NaxyVqf.png" width="250" />

Module A has also second tab - History. Here user can see all URLs that are in database. Every URL has his own status - downloaded, failed or unknown. If URL in History is red - this URL is not an image or this URL doesn't exist. And if it is green - it was downloaded fine. If URL is transparent, so checking of URL or downloading of image was failed. In this tab user may sort links by date or by status. User may go to ModuleB by clicking on the URL in History. Module B will recheck status of this link, and if it is green, URL would be deleted from the database in 15 seconds, but image would be saved in /sdcard/BIGDIG/test/B. User will get message about this action.

<img src="https://i.imgur.com/wtKYeM7.jpg" height="150" />


## Authors

* *Ivan Zolotaryov* - 3olplay@gmail.com
* *Andrii Fedorko* - andriy.fedorko01@gmail.com
* *Volodymyr Panasenko* - panvovandrik@gmail.com
* *Andrii Serbenyuk* - andriy.serb1@gmail.com
* *Danil Miniailo* - daniel_changer@yahoo.com
* *Mihail Samsonenko* - michaeldark49a@gmail.com
