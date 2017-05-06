<div class="accordion" fillSpace="sidebar">
    <div class="accordionHeader">
        <h2><span class="a05">Folder</span>统计分析</h2>
    </div>
    <div class="accordionContent">
        <ul class="tree">
            <li><a>统计分析</a>
                <ul>
                <@sec.any name="CINEMA_SETTLE_STAT">
                    <li><@dwz.a href="/stat/cinemaTicketOrderDaily-list">影院结算统计</@dwz.a></li>
                </@sec.any>
                <@sec.any name="CINEMA_SALE_STAT">
                    <li><@dwz.a href="/stat/cinemaSaleStat-list">影院销售统计</@dwz.a></li>
                </@sec.any>
                <@sec.any name="CHANNEL_SETTLE_STAT">
                    <li><@dwz.a href="/stat/channelTicketOrderDaily-list">渠道结算统计</@dwz.a></li>
                </@sec.any>
                <@sec.any name="CHANNEL_SALE_STAT">
                    <li><@dwz.a href="/stat/channelSaleStat-list">渠道销售统计</@dwz.a></li>
                </@sec.any>
                </ul>
            </li>
        </ul>
    </div>
</div>