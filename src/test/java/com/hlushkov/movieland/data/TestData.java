package com.hlushkov.movieland.data;

import com.github.database.rider.core.api.dataset.DataSetProvider;
import com.github.database.rider.core.dataset.builder.DataSetBuilder;
import org.dbunit.dataset.IDataSet;

public class TestData {

    public static class MovieProvider implements DataSetProvider {

        @Override
        public IDataSet provide() {
            return new DataSetBuilder()
                    .table("movies")
                    .columns("id", "name_russian", "name_native", "year_of_release", "description", "rating", "price", "picture_path")
                    .values("1",
                            "Побег из Шоушенка",
                            "The Shawshank Redemption",
                            "1994",
                            "Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, " +
                                    "он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их " +
                                    "рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает " +
                                    "разрабатывать невероятно дерзкий план своего освобождения.",
                            "8.9",
                            "123.45",
                            "https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg")
                    .values("2",
                            "Зеленая миля",
                            "The Green Mile",
                            "1999",
                            "Обвиненный в страшном преступлении, Джон Коффи оказывается в блоке смертников тюрьмы «Холодная гора». Вновь прибывший" +
                                    " обладал поразительным ростом и был пугающе спокоен, что, впрочем, никак не влияло на отношение к нему начальника блока" +
                                    " Пола Эджкомба, привыкшего исполнять приговор.",
                            "8.8",
                            "134.67",
                            "https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg")
                    .build();
        }
    }
}
