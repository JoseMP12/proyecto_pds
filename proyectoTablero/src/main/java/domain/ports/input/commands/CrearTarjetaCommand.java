package domain.ports.input.commands;

public record CrearTarjetaCommand(String urlTablero, String idLista, String titulo, boolean isChecklist) {}