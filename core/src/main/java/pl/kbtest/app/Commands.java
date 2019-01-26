package pl.kbtest.app;

public interface Commands {
    String SERVICE_MODE = "service";
    String DB_SERVICE_ADDRESS = "address";

    String LOAD_RULES = "load rules";
    String SHOW_RULES = "show rules";

    String ADD_FACT = "add fact";
    String LOAD_FACTS = "load facts";
    String SHOW_FACTS = "show facts";

    String FIRE_RULES = "fire rules";
    String HELP = "help";
}
