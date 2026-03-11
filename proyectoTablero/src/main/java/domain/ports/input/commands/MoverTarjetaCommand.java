package domain.ports.input.commands;

public record MoverTarjetaCommand(String urlTablero, String idTarjeta, String idListaOrigen, String idListaDestino) {}