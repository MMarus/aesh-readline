/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.aesh.readline.editing;

import org.aesh.readline.action.KeyAction;
import org.aesh.readline.terminal.Key;
import org.aesh.readline.action.Action;
import org.aesh.terminal.Device;

import java.util.Arrays;

/**
 *
 * @author <a href="mailto:stale.pedersen@jboss.org">Ståle W. Pedersen</a>
 */
public interface EditMode {

    Mode mode();

    KeyAction[] keys();

    Status status();

    void setStatus(Status status);

    Action parse(KeyAction event);

    boolean isInChainedAction();

    void updateIgnoreEOF(int eof);

    void addVariable(Variable variable, String value);

    String variableValue(Variable variable);

    EditMode addAction(Key key, Action action);

    void addAction(int[] input, String action);

    void remapKeysFromDevice(Device device);

    KeyAction prevKey();

    default KeyAction createKeyEvent(int[] input) {
        Key key = Key.getKey(input);
        if(key != null)
            return key;
        else {
            return new KeyAction() {
                private int[] key = input;

                @Override
                public int getCodePointAt(int index) throws IndexOutOfBoundsException {
                    return key[index];
                }

                @Override
                public int length() {
                    return key.length;
                }

                @Override
                public String name() {
                    return Arrays.toString(key);
                }
            };
        }
    }

    void setPrevKey(KeyAction event);

    enum Status {
        DELETE,
        MOVE,
        YANK,
        CHANGE,
        EDIT,
        COMMAND,
        HISTORY,
        SEARCH,
        REPEAT,
        // MISC
        NEWLINE,
        PASTE,
        PASTE_FROM_CLIPBOARD,
        COMPLETE,
        UNDO,
        CASE,
        EXIT,
        CLEAR,
        ABORT,
        CHANGE_EDITMODE,
        NO_ACTION,
        REPLACE,
        INTERRUPT,
        IGNORE_EOF,
        EOF,
        UP_CASE,
        DOWN_CASE,
        CAPITALIZE,
    }

    enum Mode {
        EMACS, VI
    }
}
