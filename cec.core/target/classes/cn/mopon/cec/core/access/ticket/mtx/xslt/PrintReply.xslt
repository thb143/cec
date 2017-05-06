<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/AppPrintTicketResult">
		<AppPrintTicketResult>
			<ResultCode><xsl:value-of select="ResultCode"/></ResultCode>
			<Order>
				<orderItems>
					<xsl:for-each select="SeatInfos/SeatInfo">
						<orderItem>
							<seatRow><xsl:value-of select="SeatRow" /></seatRow>
							<seatCol><xsl:value-of select="SeatCol" /></seatCol>
							<barCode><xsl:value-of select="TicketNo" /></barCode>
							<printStatus>
								<xsl:choose>
									<xsl:when test="/AppPrintTicketResult/OrderStatus='8'">1</xsl:when>
									<xsl:otherwise>0</xsl:otherwise>
								</xsl:choose>
							</printStatus>
						</orderItem>
					</xsl:for-each>
				</orderItems>
				<orderSnacks></orderSnacks>
			</Order>
		</AppPrintTicketResult>
	</xsl:template>
</xsl:stylesheet>