/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.netbeans.modules.java.source;

import com.sun.source.util.JavacTask;
import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.code.Kinds.Kind;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.CompletionFailure;
import com.sun.tools.javac.code.Symbol.ModuleSymbol;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;
import java.util.Set;
import javax.lang.model.element.ModuleElement;
import javax.lang.model.element.TypeElement;
import org.netbeans.api.java.source.CompilationInfo;

/** TODO
 *
 * @author lahvac
 */
public class ElementUtils {

    public static TypeElement getTypeElementByBinaryName(CompilationInfo info, String name) {
        return getTypeElementByBinaryName(CompilationInfoAccessor.getInstance().getJavacTask(info), name);
    }

    public static TypeElement getTypeElementByBinaryName(JavacTask task, String name) {
        Set<? extends ModuleElement> allModules = task.getElements().getAllModuleElements();
        
        if (allModules.isEmpty()) {
            Context ctx = ((JavacTaskImpl) task).getContext();
            Symtab syms = Symtab.instance(ctx);
            
            return getTypeElementByBinaryName(task, syms.noModule, name);
        }
        
        TypeElement result = null;
        
        for (ModuleElement me : allModules) {
            TypeElement found = getTypeElementByBinaryName(task, me, name);
            
            if (found != null) {
                if (result != null) return null;
                result = found;
            }
        }
        
        return result;
    }

    public static TypeElement getTypeElementByBinaryName(CompilationInfo info, ModuleElement mod, String name) {
        return getTypeElementByBinaryName(CompilationInfoAccessor.getInstance().getJavacTask(info), mod, name);
    }

    public static TypeElement getTypeElementByBinaryName(JavacTask task, ModuleElement mod, String name) {
        Context ctx = ((JavacTaskImpl) task).getContext();
        Names names = Names.instance(ctx);
        Symtab syms = Symtab.instance(ctx);
        final Name wrappedName = names.fromString(name);
        ClassSymbol clazz = syms.enterClass((ModuleSymbol) mod, wrappedName);
        
        try {
            clazz.complete();
            
            if (clazz.kind == Kind.TYP &&
                clazz.flatName() == wrappedName) {
                return clazz;
            }
        } catch (CompletionFailure cf) {
        }

        return null;
    }
}
