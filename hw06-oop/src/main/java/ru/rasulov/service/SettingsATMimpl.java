package ru.rasulov.service;

import ru.rasulov.interfaces.Cell;
import ru.rasulov.interfaces.SettingsATM;

import java.util.List;

public class SettingsATMimpl implements SettingsATM {

  private final List<Cell> cells;

  public List<Cell> getCells() {
    return cells;
  }

  public SettingsATMimpl( List<Cell> cells) {
    this.cells = cells;
  }

}
