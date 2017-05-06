<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/OnlineTicketingServiceReply/QueryCinemaReply/Cinema">
		<Cinema>
			<code><xsl:value-of select="@Code" /></code>
			<name><xsl:value-of select="@Name" /></name>
			<address><xsl:value-of select="@Address" /></address>
			<hallCount><xsl:value-of select="@ScreenCount" /></hallCount>
			<halls>
				<xsl:for-each select="Screen">
					<Hall>
						<code><xsl:value-of select="Code" /></code>
						<name><xsl:value-of select="Name" /></name>
						<seatCount><xsl:value-of select="SeatCount" /></seatCount>
					</Hall>
				</xsl:for-each>
			</halls>
		</Cinema>
	</xsl:template>
</xsl:stylesheet>