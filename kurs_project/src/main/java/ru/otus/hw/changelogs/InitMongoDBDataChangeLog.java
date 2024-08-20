package ru.otus.hw.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Child;
import ru.otus.hw.models.EducationalService;
import ru.otus.hw.models.Person;
import ru.otus.hw.models.Teacher;
import ru.otus.hw.repositories.ChildRepository;
import ru.otus.hw.repositories.EducationalServiceRepository;
import ru.otus.hw.repositories.PersonRepository;
import ru.otus.hw.repositories.TeacherRepository;

import java.util.Calendar;
import java.util.Set;
import java.util.stream.Stream;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

    @ChangeSet(order = "000", id = "dropDB", author = "user1", runAlways = false)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }


    @ChangeSet(order = "001", id = "initChild", author = "user1", runAlways = false)
    public void initChild(ChildRepository childRepository,
                          PersonRepository personRepository,
                          TeacherRepository teacherRepository,
                          EducationalServiceRepository educationalServiceRepository) {

        final String id = "1";

        var person = new Person(id, "Dad", "79126546789", null);
        var child = new Child(id, "child name", "79305778901", Set.of(person));
        person.setChilds(Set.of(child));
        var teacher = new Teacher(id, "primary school teacher", "79099945678");

        Calendar c1 = Calendar.getInstance();
        c1.set(2024, Calendar.JULY, 17);

        personRepository.save(person).block();
        childRepository.save(child).block();
        teacherRepository.save(teacher).block();
        Stream.of(
                        "Математика",
                        "Русский",
                        "Продлёнка",
                        "Аппликации",
                        "Питание")
                .forEach(serv -> educationalServiceRepository.save(
                        new EducationalService(null, serv, false)
                ).block());
    }
}
