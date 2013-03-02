package fr.nc333.planetpvp;

public class ETools
{
  public static int getEnchantIdByName(String enchantname)
  {
    String str = enchantname; switch (enchantname.hashCode()) { case -1969960471:
      if (str.equals("projectile"));
      break;
    case -1727707761:
      if (str.equals("fireprotection"));
      break;
    case -1697088540:
      if (str.equals("aquaaffinity"));
      break;
    case -1684858151:
      if (str.equals("protection"));
      break;
    case -1571105471:
      if (str.equals("sharpness"));
      break;
    case -1339126929:
      if (str.equals("damage"));
      break;
    case -1083815289:
      if (str.equals("falling"));
      break;
    case -677216191:
      if (str.equals("fortune"));
      break;
    case -226555378:
      if (str.equals("fireaspect"));
      break;
    case 93819384:
      if (str.equals("blast"));
      break;
    case 97513267:
      if (str.equals("flame"));
      break;
    case 107028782:
      if (str.equals("punch"));
      break;
    case 173173288:
      if (str.equals("infinity"));
      break;
    case 350056506:
      if (str.equals("looting"));
      break;
    case 915847580:
      if (str.equals("respiration"));
      break;
    case 961218153:
      if (str.equals("efficiency")) break; break;
    case 976288699:
      if (str.equals("knockback"));
      break;
    case 1147645450:
      if (str.equals("silktouch"));
      break;
    case 1565882285:
      if (str.equals("vsarthropods"));
      break;
    case 1603571740:
      if (str.equals("unbreaking"));
      break;
    case 1931174778:
      if (!str.equals("vsundead")) { break label493;

        return 32;
        return 34;
        return 35;
        return 33;
        return 21;
        return 20;
        return 19;
        return 18; } else {
        return 17;
        return 16;

        return 48;
        return 50;
        return 49;
        return 51;

        return 5;
        return 6;
        return 4;
        return 0;
        return 3;
        return 2;
        return 1;
      }break; }
    label493: return -1;
  }

  public static String canEnchant(int itemid)
  {
    switch (itemid) {
    case 268:
      return "tool";
    case 269:
      return "tool";
    case 270:
      return "tool";
    case 271:
      return "tool";
    case 290:
      return "tool";
    case 272:
      return "tool";
    case 273:
      return "tool";
    case 274:
      return "tool";
    case 275:
      return "tool";
    case 291:
      return "tool";
    case 256:
      return "tool";
    case 257:
      return "tool";
    case 258:
      return "tool";
    case 267:
      return "tool";
    case 292:
      return "tool";
    case 283:
      return "tool";
    case 284:
      return "tool";
    case 285:
      return "tool";
    case 286:
      return "tool";
    case 294:
      return "tool";
    case 276:
      return "tool";
    case 277:
      return "tool";
    case 278:
      return "tool";
    case 279:
      return "tool";
    case 293:
      return "tool";
    case 261:
      return "bow";
    case 298:
      return "armor";
    case 299:
      return "armor";
    case 300:
      return "armor";
    case 301:
      return "armor";
    case 302:
      return "armor";
    case 303:
      return "armor";
    case 304:
      return "armor";
    case 305:
      return "armor";
    case 306:
      return "armor";
    case 307:
      return "armor";
    case 308:
      return "armor";
    case 309:
      return "armor";
    case 310:
      return "armor";
    case 311:
      return "armor";
    case 312:
      return "armor";
    case 313:
      return "armor";
    case 314:
      return "armor";
    case 315:
      return "armor";
    case 316:
      return "armor";
    case 317:
      return "armor";
    case 259:
    case 260:
    case 262:
    case 263:
    case 264:
    case 265:
    case 266:
    case 280:
    case 281:
    case 282:
    case 287:
    case 288:
    case 289:
    case 295:
    case 296:
    case 297: } return "unknow";
  }

  public static double round(double d) {
    double round = d * 100.0D;
    round = Math.round(round);
    round /= 100.0D;
    return round;
  }

  public static boolean isInteger(String input)
  {
    try
    {
      Integer.parseInt(input);
      return true;
    }
    catch (Exception localException) {
    }
    return false;
  }

  public static boolean isCorrectEnchantment(String name) {
    String str = name; switch (name.hashCode()) { case -1969960471:
      if (str.equals("projectile"));
      break;
    case -1727707761:
      if (str.equals("fireprotection"));
      break;
    case -1697088540:
      if (str.equals("aquaaffinity"));
      break;
    case -1684858151:
      if (str.equals("protection"));
      break;
    case -1571105471:
      if (str.equals("sharpness"));
      break;
    case -1339126929:
      if (str.equals("damage"));
      break;
    case -1083815289:
      if (str.equals("falling"));
      break;
    case -677216191:
      if (str.equals("fortune"));
      break;
    case -226555378:
      if (str.equals("fireaspect"));
      break;
    case 93819384:
      if (str.equals("blast"));
      break;
    case 97513267:
      if (str.equals("flame"));
      break;
    case 107028782:
      if (str.equals("punch"));
      break;
    case 173173288:
      if (str.equals("infinity"));
      break;
    case 350056506:
      if (str.equals("looting"));
      break;
    case 915847580:
      if (str.equals("respiration"));
      break;
    case 961218153:
      if (str.equals("efficiency")) break; break;
    case 976288699:
      if (str.equals("knockback"));
      break;
    case 1147645450:
      if (str.equals("silktouch"));
      break;
    case 1565882285:
      if (str.equals("vsarthropods"));
      break;
    case 1603571740:
      if (str.equals("unbreaking"));
      break;
    case 1931174778:
      if (!str.equals("vsundead")) { break label478;

        return true;
        return true;
        return true;
        return true;
        return true;
        return true;
        return true;
        return true;
        return true;
        return true;
        return true;
        return true; } else {
        return true;
        return true;
        return true;
        return true;
        return true;
        return true;
        return true;
        return true;
        return true;
      }break; }
    label478: return false;
  }
}