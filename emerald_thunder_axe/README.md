# Emerald Thunder Axe - Minecraft 26.2 NeoForge Mod

## Características

### Hacha Esmeralda del Trueno
- **Textura épica** verde con efectos de rayo
- Al **golpear mobs hostiles** → lanza una línea de **8 truenos** hacia adelante
- Al golpear mobs pacíficos → no hace nada especial
- **Click derecho** → Invoca un **Enderman Verde** aliado con aura verde
- El Enderman Verde:
  - Tiene 50 de vida
  - Hace 9 de daño
  - Te sigue y pelea por ti
  - Se teletransporta si te alejas demasiado
  - Tiene aura de partículas verdes
  - Cooldown de invocación: 15 segundos

### Crafteo
```
D E D
  S  
  S  
```
- D = Diamante
- E = Esmeralda
- S = Palo

## Cómo instalar / desarrollar

1. Asegúrate de tener **Java 25** instalado
2. Clona o descarga este proyecto
3. Abre la carpeta en IntelliJ IDEA (recomendado 2025.2+)
4. Espera a que Gradle sincronice
5. Corre la configuración `runClient`

## Estructura del proyecto

```
emerald_thunder_axe/
├── src/main/java/com/example/emeraldthunderaxe/
│   ├── EmeraldThunderAxeMod.java          (clase principal)
│   ├── item/
│   │   ├── EmeraldThunderAxeItem.java     (lógica del hacha)
│   │   ├── ModItems.java
│   │   └── ModToolTiers.java
│   └── entity/
│       ├── GreenEndermanEntity.java       (Enderman aliado)
│       ├── ModEntities.java
│       └── ModEntityAttributes.java
└── src/main/resources/
    ├── assets/emeraldthunderaxe/
    │   ├── lang/
    │   ├── models/item/
    │   └── textures/item/                 ← aquí va la textura
    └── data/emeraldthunderaxe/recipe/
```

## Notas importantes

- La textura del hacha debe llamarse `emerald_thunder_axe.png` (16x16 o 32x32)
- Colócala en: `src/main/resources/assets/emeraldthunderaxe/textures/item/`
- El mod usa NeoForge 26.2.0.93

¡Disfruta destruyendo todo con truenos verdes!
