package com.ilkayaktas.margatsni.controller.strategy;

import java.io.Serializable;

/**
 * Created by ilkay on 03/04/2017.
 */

public interface Strategy extends Serializable {
	String execute();
}