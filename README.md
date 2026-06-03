📢 Plasmo Voice Addon – Custom Overlay Name

This project is an addon for [Plasmo Voice](https://modrinth.com/plugin/plasmo-voice)
that lets players customize their display name. Instead of always showing their
Minecraft username, players can set their own nickname that appears in the voice
chat overlay **and**, as of this update, on the tab list ("scoreboard") and in
chat — all updated in real time.

✨ Features

- 🔧 Custom Overlay Name – Change your name in-game with:

<pre>/nick set &lt;your nickname&gt;</pre>

  No quotes needed — everything after `set` is used as-is, spaces included
  (e.g. `/nick set Night Fall King`).

- 🏷️ Real-time tab-list rename – Your nickname now replaces your name on the
  player list / tab "scoreboard" the instant you set it, no relog required.

- 💬 Real-time chat rename – Messages you send show your nickname as the sender.

- 🎨 Team colors kept – if you're on a scoreboard team, the nickname keeps that
  team's color/prefix/suffix on the tab list and in chat.

- 🔎 Operator GUI – ops run `/nick look` to open a chest GUI listing every online
  player as a head: the item name is their nickname and the lore shows their real
  Minecraft username. (Server-side; works on a vanilla client, view-only.)

- 🔁 Reload from disk – Ops can re-read `config/nicknames.json` with `/nick reload`.

- 📦 One jar, both loaders – A single universal jar runs on **Forge** and
  **Fabric** (Minecraft 1.20.1). Drop the same file into either loader's `mods`
  folder.

- 👥 Unique Per Player – Every player can have their own custom nickname.

- 💻 Server-Side Only – Install on the server only; players don't need the mod.

- ⚠️ Work in Progress – Bugs may still occur.

- 📌 Note – This does not change the name tag floating above the player's head;
  it changes the tab list, chat and the Plasmo Voice overlay.

📥 Installation

<pre>Works on Minecraft 1.20.1 (Forge 47.x or Fabric).

For the voice-overlay feature, install Plasmo Voice (2.x) on the server.
The tab-list and chat rename features work even without Plasmo Voice.

Place the universal jar in the server's mods folder and restart the server.</pre>

📝 Usage

<pre>Join the server and run:

/nick set &lt;your nickname&gt;

(Spaces are allowed and no quotes are needed, e.g. "/nick set Night Fall King".)

Your nickname now appears on the tab list, in chat, and in the
Plasmo Voice overlay whenever you speak.

Operators can run /nick look to open a GUI listing every online
player's nickname and their real Minecraft username.</pre>

🛠️ Building

<pre>./gradlew build

The mod lives in two subprojects (:fabric and :forge) so each loader can be
built and run on its own; loader-agnostic code is shared from src/shared.

Outputs:
  fabric/build/libs/nightfalloverlay-&lt;version&gt;.jar       Fabric jar
  forge/build/libs/nightfalloverlay-forge-&lt;version&gt;.jar  Forge jar
  build/libs/nightfalloverlay-&lt;version&gt;-universal.jar    Combined jar for BOTH loaders</pre>

🧪 Testing with two players (dev)

<pre>Each loader has a second client run config ("Minecraft Client 2", username
Player2) next to the default "Minecraft Client" and "Minecraft Server".

  1. Start the server:
       Fabric: ./gradlew :fabric:runServer      Forge: ./gradlew :forge:runServer
  2. Start both clients:
       Fabric: ./gradlew :fabric:runClient  and  ./gradlew :fabric:runClient2
       Forge:  ./gradlew :forge:runClient   and  ./gradlew :forge:runClient2
  3. Connect both to localhost (Multiplayer → Direct Connect → 127.0.0.1)
  4. As one player run:  /nick set Some Name
     and watch it change on the OTHER player's tab list and chat in real time.
     Ops can run /nick look to see everyone's nickname + real username.</pre>

📌 Notes

<pre>Your Minecraft username does not change – only the displayed nickname.
This addon is cosmetic only.</pre>

📜 License

This project is licensed under the CC0-1.0 License – free to use, modify, and share.
