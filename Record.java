public class Record {
    private int game_date_est; // 4 bytes
    private int team_id_home; // 4 bytes
    private short pts_home; // 1 byte
    private float fg_pct_home; // 4 bytes
    private float ft_pct_home; // 4 bytes
    private float fg3_pct_home; // 4 bytes
    private byte ast_home; // 1 byte
    private byte reb_home; // 1 byte
    private byte home_team_wins; // 1 byte

    public Record(int game_date_est, int team_id_home, short pts_home, float fg_pct_home, float ft_pct_home, float fg3_pct_home, byte ast_home, byte reb_home, byte home_team_wins) {
        this.game_date_est = game_date_est;
        this.team_id_home = team_id_home;
        this.pts_home = pts_home;
        this.fg_pct_home = fg_pct_home;
        this.ft_pct_home = ft_pct_home;
        this.fg3_pct_home = fg3_pct_home;
        this.ast_home = ast_home;
        this.reb_home = reb_home;
        this.home_team_wins = home_team_wins;
    }

    // Getter for game_date_est
    public int getGame_date_est() {
        return game_date_est;
    }

    // Getter for team_id_home
    public int getTeam_id_home() {
        return team_id_home;
    }

    // Getter for pts_home
    public short getPts_home() {
        return pts_home;
    }

    // Getter for fg_pct_home
    public float getFg_pct_home() {
        return fg_pct_home;
    }

    // Getter for ft_pct_home
    public float getFt_pct_home() {
        return ft_pct_home;
    }

    // Getter for fg3_pct_home
    public float getFg3_pct_home() {
        return fg3_pct_home;
    }

    // Getter for ast_home
    public byte getAst_home() {
        return ast_home;
    }

    // Getter for reb_home
    public byte getReb_home() {
        return reb_home;
    }

    // Getter for home_team_wins
    public byte getHome_team_wins() {
        return home_team_wins;
    }
}