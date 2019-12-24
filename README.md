# AndroidWakeUpTest
一時的なプロジェクト。後で非公開にする

ForegroundServiceとして10秒定期イベントを実行する。
実行内容：
- ディスプレイのスリープを3秒間解除
- Chromeアプリへpending intentを指定してNotificationを発行

Notificationを選択すると、chromeが立ち上がる。
画面がロックされていた場合はロックの解除画面になり、解除するとChromeになる。
※Chromeの立ち上げ自体はロックの解除と関係なく後ろで実行されているもよう

