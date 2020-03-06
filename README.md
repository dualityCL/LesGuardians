# LesGuardians
![Version](https://img.shields.io/badge/Version-2.7.5_R2-purple.svg) ![Build Status](https://img.shields.io/badge/build-FINAL-purple.svg)

<p align="center"><img src="https://i.imgur.com/SubpjnI.png" width="50%"><br>Escrito en Java</p>

# Introducción (Motivo del proyecto y una pequeña declaración de copyright)
Inicio este proyecto porque los desarrolladores han dejado de trabajar en él a finales del año 2012 y a modo de aprendizaje decidí decompilar la última versión publicada por ellos, la v2.7.5 (compilada el 28 de novimebre de 2012).

Todos los derechos de este proyecto pertenecen a los desarrolladores originales, si vas a trabajar con estos archivos tienes que darle crédito a los desarrolladores originales. He dejado datos para contactarlos, revísalos haciendo [clic aquí](https://github.com/RSPAWN/LesGuardians#cr%C3%A9ditos).

# Changelog 

🌀 v2.7.5-R2 (03/03/2020 - 05/03/2020) - VERSIÓN ESTABLE
* Puedes descargar el emulador completo reorganizado (jar, src, db, clips, lang) haciendo [clic aquí](https://github.com/RSPAWN/LesGuardians/releases/tag/v2.7.5-R2), donde también encontrarás el changelog completo de dicha versión.

🌀 v2.7.5-R1 - Versión base (02/03/2020)
* Esta versión es el resultado de la decompilación del JAR, por lo tanto los errores están a la orden del día en el código fuente.
* El servidor ni siquiera puede iniciarse correctamente, durante cierto punto ya era posible pero el Pathfinding estaba totalmente estropeado al igual que varios objetos interactivos.
* Comandos: Corrección aplicada a los comandos "who", "SHOWFIGHTPOS", "TELEPORT", "IRMAPA", "ENERGIA", "ITEM", "KAMAS", "ENCHERVIDA" y "LEVEL".
* Constantes: Corrección aplicada a los métodos "getObjetoPorTrabajo" y "esRetoPosible1".
* IA: Reconstrucción de los métodos "listaEnemigosMenosPDV", "listaTodoLuchadores" y "objetivosMasCercanos".
* SQLManager: Reconstrucción de los métodos "analizarHechizoStats" y "ejecutarConsultaSQL".
* GameThread: Correcciones menores.
* Acción: Corregido constructor Prisma en "case 201".
* Maps: Corrección aplicada a los métodos "delAccionFinPelea", "getObjectosGDF", "refrescarGrupoMobs" y "copiarMapa".
* ConsolePersonalizado: Reparado void run().
* Conta: Reconstrucción del método "resetTodosPjs".
* Pathfinding: Reconstrucción de los métodos "numeroMovimientos", "checkearLineaDeVista", "listaCeldasDesdeLuchador" y "getDirEntreDosCeldas".

A continuación se encuentran listados todos los cambios realizados por los autores originales del proyecto:

🌀 Versión 2.7.5 (~2012)
* Bonificaciones de conjuntos (sets) de clase (60%).
* Comandos para Desbug (iniciarluta y passarturno con condiciones anti-bug).
* Cercados (cruzar monturas con objetos de cría).
* Prismas.
* Reconexión en combate.
* Reinicio automático de servidor, configurable.
* Sistema de soporte (50%).
* Lang.
* Estabilidad mejorada.
* IA trabajada (95%).
* Todas las acciones de objetos desbugeadas.
* Tienda in-game.
* Vídeo tutorial.
* Territorios conquistables.
* Modo heroíco.
* Comandos nuevos en la consola.
* Consola (CMD) personalizada.
* Items 2.0/2.8 (Actualizaciones constantes).

🌀 v2.5.0
* Objetos/Acciones: Corrección de los pergaminos, no multipliques y no hagas spam de su efecto en el chat.
* Sistema: Creación de multiservidor para pruebas internas por parte del Staff.
* Sistema: Sistema de alimentación de mascotas creado (ahora es necesario alimentar tu mascota para que gane stats).
* Objetos/Acciones: Objetos de mejora creados para las mascotas (aumenta el límite de stats).
* Lucha/Personaje: El personaje ya no comienza automáticamente una pelea si aparece en una casilla ocupada por un grupo de monstruos.
* Lucha/Personaje/Hechizo: Corrección de desbuffs de hechizos.
* Objetos/Acciones: Corrección de todas las viñetas (Energético, + Estado, etc.)
* Lucha: Se creó un método que bloquea el acceso al modo espectador de las peleas.
* Servidor: comandos de teletransporte rediseñados, con restricción para teletransportarse dentro de una pelea.
* Lucha: Espectador logra ver los rtos actuales de la pelea.
* Objetos/Acciones: Se agregó acción de aura de vampiro al colocar una de las partes del Set Vampyro.
* Personaje: El personaje se teletransporta al cementerio más cercano cuando muere.
* Sistema: Mejorado el sistema VIP del servidor, ahora tiene áreas premium para VIPS. (Dentro del área premium, el jugador normal no puede pelear contra mobs, hacer PvP, entre otras cosas).
* Fórmula: Añadida fórmula donde cada HIT tomado disminuirá la vitalidad FIJA. (Evitando así peleas interminables entre Ani vs Ani entre otros...).
* Sistema: Recaudadores (reajustada la frase cuando el recaudador es atacado. se corrigió el panel de defensa en el gremio y la función para recolectar.
* Sistema reconexión automática creado cuando el servidor sufre cierta inestabilidad. (Evitando que se pierda la conexión).

🌀 v2.3.4
* Se corrigió la creación de personajes.
* Corrección de problemas SQL.
* Se eliminaron los prefijos en la base de datos (ahora es posible usar cualquier CMS).

🌀 v2.3.3
* Comandos fijos: .astrub .shop y .criarguilda.
* Corregidos hechizos especiales de clase al 80%.
* Se arregló la invocación de Chaferloko.
* Se creó el .exe del emulador.
* Método de comando de jugador rediseñado.

🌀 v2.3.0
* Corección de objetos con acciones (se implementaron +12 pociones de teletransporte): 25%.
* Traducción de pociones de mazmorras: 85%
* Se agregaron +65 elementos 2.0 con estado listo en la base de datos.
* Creación de lang propio para el emulador.
* Bases de datos (static y dynamic) ahora están en una sola (peor idea).
* Reformulación de SocketManager: Detallando todos los procesos del juego por el emulador en portugués.
* Reformulación de SQLManager.
* Sistemas de prismas: 50%.

🌀 v2.2.0
* Sistema de prismas 30%.
* Sistema de mascotas alimentables 60%.
* Sistema de mensajes automáticos 100%.
* Sistema de regalos 100%.
* Bloquea los comandos del sistema de los jugadores a través de la configuración.
* Mensaje de bienvenida 100%.
* Set inicial ajustable por config.
* Se corrigieron las invocaciones del Osamodas al 100%.
* Se corrigieron los objevivos al 100%.
* Se corrigió el comando .life 100%.
* Se corrigió la lista de amigos al 100%.
* Recaudadores corregidos (defender recaudador idéntico al servidor oficial).
* Matrimonios corregidos (teletransportarse usando el botón de unión).

🌀 v2.0.0
* Mobs con estrellas.
* Modo comerciante 100%.
* Retos 100%.
* Hechizos 90% 
* Interactividad del emulador con el servidor: Comando mute envía mensajes al chat global del juego, lo mismo sucede cuando se otorgan títulos.
* Todos los logs del servidor están en portugués (si un jugador se tira un pedo, el personal lo sabe).
* Experiencia de los mobs ajustada.
* Nuevos comandos de chat: .a (chat global), .v (chat VIP) y .e (chat de eventos).  
* Más de 40 comandos.

🌀 v1.5.0
* Retos 75%.
* Modo comerciante 90% (bug al quitar modo comerciante).
* Objevivos 100%
* Cofres de casas 100%.

🌀 v1.0.5
* Se corrigió un error al crear personajes.
* Se corrigió un error cuando se colocaba/retiraba un recaudador.

🌀 v1.0.0
* Casamiento.
* Dragopavo camaleón.
* Cerca de 40 items VIP en la DB + NPC que los vende.
* Casas.
* Cercados.
* Recaudadores (90% solo con nombre undefined).
* Captura de almas.
* Invocar mobs.
* Títulos.
* Oficios.
* Solucionado error de conexión interrumpida cuando el servidor tiene lag.
* Nivel máximo de personaje configurable.
* Objetivos (alvos en PT) que dan experiencia.
* Comandos para todos los territorios.
* Nuevos comandos para el jugador: .enchervida y .staff.
* Nuevos comandos para la consola: block (solo los administradores pueden conectarse al servidor), lock (libera el servidor), shutdown (vuelve a abrir el servidor a la hora deseada) y trigger (agrega triggers sin tener que hacerlo desde la DB).
* Emulador portugués.

# Créditos

El emulador fue desarrollado originalmente por EduardoLBS (eduardo.lbs@live.com) y Samuka (samuel@dpbrasil.net) basándose en el emulador Elbustemu. 

Si lo deseas, puedes visitar la página oficial de los desarrolladores haciendo [clic aquí](http://www.lesguardiansemu.xpg.com.br/).
