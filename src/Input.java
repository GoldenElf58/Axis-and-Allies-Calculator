import java.util.EnumMap;
import java.util.Map;
import java.util.Scanner;

public class Input {

    static boolean getSeaBattle(Scanner sc) {
        System.out.print("Is this a sea battle? (y/N): ");
        return sc.nextLine().trim().equalsIgnoreCase("y");
    }

    static Map<UnitType, Integer> readArmy(Scanner sc, String side,
                                           boolean defender, boolean seaBattle) {
        Map<UnitType, Integer> map = new EnumMap<>(UnitType.class);
        for (UnitType t : UnitType.values()) {
            if (seaBattle && t.landOnly) continue;
            if (!seaBattle && t.seaOnly) continue;

            System.out.print(side + " " + t.name().toLowerCase() + ": ");
            String s = sc.nextLine().trim();
            map.put(t, s.isEmpty() ? 0 : Integer.parseInt(s));
        }
        for (UnitType t : UnitType.values()) {
            map.putIfAbsent(t, 0);
        }

        if (seaBattle && defender) {
            int fighters = map.get(UnitType.FIGHTER);
            int carriers = map.get(UnitType.CARRIER);
            if (fighters > carriers * 2) {
                System.out.println("Invalid: defenders may only have 2 fighters per carrier.");
                return readArmy(sc, side, true, true);
            }
        }
        return map;
    }
}
