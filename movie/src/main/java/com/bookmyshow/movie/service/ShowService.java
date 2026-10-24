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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class ShowService {

    private final ShowRepository showsRepository;
    private final MovieRepository movieRepository;
    private final SeatRepository seatRepository;
    private final ScreenRepository screenRepository;
    private final PhysicalSeatRepository physicalSeatRepository;

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

        int requestedFrom = getSeatNumber(seatRange.getFromSeat());
        int requestedTo = getSeatNumber(seatRange.getToSeat());

        // TODO: Later these will come from SecurityContext.
        String theaterId = "TH001";
        String screenName = "Screen 1";

        String dbSeatFrom = physicalSeatRepository.findDbSeatFrom(theaterId,screenName);
        String dbSeatTo = physicalSeatRepository.findDbSeatTo(theaterId,screenName);

        int dbFrom = getSeatNumber(dbSeatFrom);
        int dbTo = getSeatNumber(dbSeatTo);

        return requestedFrom >= dbFrom
                && requestedTo <= dbTo;
    }

    private int getSeatNumber(String seatId) {
        return Integer.parseInt(seatId.substring(6));
    }

    private void allotSeats(
            String showId,
            SeatType seatType,
            SeatRange seatRange) {

        int from = getSeatNumber(seatRange.getFromSeat());
        int to = getSeatNumber(seatRange.getToSeat());


        List<ShowSeat> showSeats = new ArrayList<>();

        for (int i = from; i <= to; i++) {

            ShowSeat showSeat = new ShowSeat();

            showSeat.setShowId(showId);
            showSeat.setSeatId("seatId" + i);
            showSeat.setStatus(SeatStatus.available);
            showSeat.setPrice(BigDecimal.valueOf(200));

            showSeats.add(showSeat);
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
}
