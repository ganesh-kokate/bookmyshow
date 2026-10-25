package com.bookmyshow.movie.service;

import com.bookmyshow.common.enums.SeatStatus;
import com.bookmyshow.common.models.*;
import com.bookmyshow.common.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class ShowService {

    private final ShowRepository showsRepository;
    private final MovieRepository movieRepository;
    private final SeatRepository seatRepository;
    private final ScreenRepository screenRepository;
    private final PhysicalSeatRepository physicalSeatRepository;
    private record SeatPosition(char row, int column) {}

        public Shows creatShow(AddShowRequest request)
        {
            Movie movie = movieRepository.findById(request.movieId())
                    .orElseThrow(() -> new RuntimeException("Movie not found"));

            // TODO: Later these will come from SecurityContext.
            String theaterId = "TH010";
            String screenName = "Screen 1";

            Screen screen = screenRepository
                    .findByTheaterIdAndScreenName(theaterId, screenName).orElseThrow(() -> new RuntimeException("Screen not found"));;

            LocalDateTime endTime = request.startTime()
                    .plusMinutes(movie.getDuration());

            String showId = generateShowId();

            Shows show = Shows.builder()
                        .showId(showId)
                        .movie(movie)
                        .screen(screen)
                        .startTime(request.startTime())
                        .endTime(endTime)
                        .build();

            return showsRepository.save(show);
        }

        public List<Shows> getShowByTheater(String theatreId)
        {
            return showsRepository.findByTheaterId(theatreId);
        }

    private String generateShowId() {
        return "SHOW" + System.currentTimeMillis();
    }

    @Transactional
    public String allotSeatsForShow(AllotSeatRequest request) {

        String showId = request.showId();

        // Validate every range before inserting seats
        for (Map.Entry<SeatType, SeatRange> entry : request.seats().entrySet()) {

            SeatType seatType = entry.getKey();
            SeatRange seatRange = entry.getValue();

            if (!isSeatRangeAvailable(seatRange)) {
                return "Fail";
            }

            allotSeats(showId, seatType, seatRange);
        }

        return "Success";
    }

    private boolean isSeatRangeAvailable(SeatRange seatRange) {

        SeatPosition requestedFrom = getSeatPosition(seatRange.getFromSeat());
        SeatPosition requestedTo = getSeatPosition(seatRange.getToSeat());

        if (SEAT_COMPARATOR.compare(requestedFrom, requestedTo) > 0) {
            return false;
        }

        // TODO: Later these will come from SecurityContext.
        String theaterId = "TH001";
        String screenName = "Screen 1";

        String dbSeatFrom = physicalSeatRepository.findDbSeatFrom(theaterId,screenName);
        String dbSeatTo = physicalSeatRepository.findDbSeatTo(theaterId,screenName);

        SeatPosition dbFrom = getSeatPosition(dbSeatFrom);
        SeatPosition dbTo = getSeatPosition(dbSeatTo);


        return SEAT_COMPARATOR.compare(requestedFrom, dbFrom) >= 0
                && SEAT_COMPARATOR.compare(requestedTo, dbTo) <= 0;
    }


    private SeatPosition getSeatPosition(String seatId) {

        char row = seatId.charAt(0);

        int column = Integer.parseInt(seatId.substring(1));

        return new SeatPosition(row, column);
    }


    private void allotSeats(
            String showId,
            SeatType seatType,
            SeatRange seatRange) {

        SeatPosition from = getSeatPosition(seatRange.getFromSeat());
        SeatPosition to = getSeatPosition(seatRange.getToSeat());
        BigDecimal price = seatRange.getPrice();

        System.out.println(seatRange.getPrice());
        List<ShowSeat> showSeats = new ArrayList<>();

        for (char row = from.row(); row <= to.row(); row++) {

            int startColumn =  (row == from.row()) ? from.column() : 1;
            int endColumn =    (row == to.row()) ? to.column() : 10;

            for (int column = startColumn; column <= endColumn; column++)
            {

                String seatId = row + String.valueOf(column);

                ShowSeat showSeat = new ShowSeat();

                showSeat.setShowId(showId);
                showSeat.setSeatId(seatId);
                showSeat.setStatus(SeatStatus.available);
                showSeat.setPrice(price);

                showSeats.add(showSeat);
            }
        }

        seatRepository.saveAll(showSeats);
    }

    public String removeShow(String showId) {
            showsRepository.deleteById(showId);
            return "Success";
    }

    public List<MovieShowsDTO> getShowsforMovie(int movieId) {

        List<MovieShowRowDTO> rows =showsRepository.findMovieShows(movieId);

        Map<String, MovieShowsDTO> theaterMap = new LinkedHashMap<>();

        for (MovieShowRowDTO row : rows) {
            MovieShowsDTO theater = theaterMap.computeIfAbsent(
                    row.getTheaterId(),
                    id -> {
                        MovieShowsDTO dto = new MovieShowsDTO();
                        dto.setTheaterId(id);
                        dto.setTheaterName(row.getTheaterName());
                        dto.setTheaterCity(row.getTheaterCity());
                        return dto;
                    }
            );

            ShowDTO show = new ShowDTO();
            show.setShowId(row.getShowId());
            show.setScreenName(row.getScreenName());
            show.setStartTime(row.getStartTime());
            show.setEndTime(row.getEndTime());
            theater.getShows().add(show);
        }
        return new ArrayList<>(theaterMap.values());
    }

    private static final Comparator<SeatPosition> SEAT_COMPARATOR =
            Comparator.
                    comparingInt((SeatPosition s) -> s.row())
                    .thenComparingInt(SeatPosition::column);

}
