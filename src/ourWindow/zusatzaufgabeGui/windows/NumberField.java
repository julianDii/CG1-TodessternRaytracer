/*
 * MIT License
 *
 * Copyright (c) 2016 Julian Dobrot
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package ourWindow.zusatzaufgabeGui.windows;

import javafx.scene.control.TextField;



public class NumberField extends TextField{

	private static final String regex = "[+-]?[\\d]+[\\.]?[\\d]*";

	public NumberField(){
		this("0");
	}

	public NumberField(String text){
		setMaxSize(50, 10);
		setText(text);
		setNumber();
		setOnMouseClicked(e -> this.clear());
		focusedProperty().addListener(event2 -> setNumber());
	}

	private void setNumber(){
		String text = getText();
		if (!text.matches(regex)||text==null) {
			this.setText("0");
		} else {
			this.setText(text);
		}
	}

	public double getNumber(){
		System.out.println(Double.parseDouble(getText()));
		return Double.parseDouble(getText());
	}
}
