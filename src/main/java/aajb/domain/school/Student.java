/*
 * Copyright 2007-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package aajb.domain.school;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;


/**
 * Annotation configured person student bean.
 * 
 * @author David Winterfeldt
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@PrimaryKeyJoinColumn(name="ID")
public class Student extends Person {

    @Enumerated(value = EnumType.STRING)
    private ClassName className;

    private String remarks;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FirstParent_ID")
    private Parent firstParent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SecondParent_ID")
    private Parent secondParent;

    @ManyToOne(fetch = FetchType.LAZY)
    private Registration registration;
}
